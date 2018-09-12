package com.drag.xiongji.task;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.drag.xiongji.common.exception.AMPException;
import com.drag.xiongji.pay.PayReturn;
import com.drag.xiongji.pt.dao.PtGoodsDao;
import com.drag.xiongji.pt.dao.PtOrderDao;
import com.drag.xiongji.pt.dao.PtUserDao;
import com.drag.xiongji.pt.entity.PtGoods;
import com.drag.xiongji.pt.entity.PtOrder;
import com.drag.xiongji.pt.entity.PtUser;
import com.drag.xiongji.utils.StringUtil;

import lombok.extern.slf4j.Slf4j;
/**
 * 定时任务查询拼团有效期，退款
 * @author longyunbo
 *
 */
@Slf4j
@Component
public class PtValidhoursTask {
	
	@Autowired
	PtGoodsDao ptGoodsDao;
	@Autowired
	PtUserDao ptUserDao;
	@Autowired
	PtOrderDao ptOrderDao;
	
	@Scheduled(cron = "${jobs.hoursCheckTask.schedule}")
	@Transactional
	public void find() {
		
		try {
			Date nowTime = new Timestamp(System.currentTimeMillis());
			List<PtGoods> ptGoodsList = ptGoodsDao.findByIsEnd(0);
			for (PtGoods ptGoods : ptGoodsList) {
				//拼团有效时间，默认为24小时
				int ptValidhours = ptGoods.getPtValidhours();
				int goodsId = ptGoods.getPtgoodsId();
				//根据商品编号查询出拼团中的团长，如果团长的创建时间过期了，把整个拼团的关掉
				List<PtUser> userList = ptUserDao.findByGoodsIdAndStatusAndIsHeader(goodsId, PtUser.PTSTATUS_MIDDLE,PtUser.ISHEADER_YES);
				Set<String> ptcodes = new HashSet<String>();
				//购买多少份
				int number = 0;
				if(userList != null && userList.size() > 0) {
					for(PtUser user : userList) {
						Date createTime =  user.getCreateTime();
						long compareDate = (nowTime.getTime() - createTime.getTime()) / (60*60*1000);
						if(compareDate >= ptValidhours) {
							//修改为拼团失败
							ptcodes.add(user.getPtcode());
						}
					}
					if(ptcodes != null && ptcodes.size() > 0) {
						List<PtUser> childList =  ptUserDao.findByPtCodeIn(ptcodes);
						if(childList != null && childList.size() > 0) {
							for(PtUser user : childList) {
								user.setPtstatus(PtUser.PTSTATUS_FAIL);
								ptUserDao.saveAndFlush(user);
							}
						}
					}
				}
				if(ptcodes != null && ptcodes.size() > 0) {
					List<PtOrder> orders = ptOrderDao.findByPtCodeIn(ptcodes);
					for(PtOrder order : orders) {
						number = number + order.getNumber();
						String ptrefundcode = order.getPtrefundcode();
						if(StringUtil.isEmpty(ptrefundcode)) {
							String out_trade_no = order.getOutTradeNo();
							BigDecimal num = new BigDecimal(order.getNumber());
							BigDecimal price = order.getPrice();
							//单位以分计算
							BigDecimal totalPrice = price.multiply(num).multiply(new BigDecimal(100));
							//退款
							JSONObject returnJson = PayReturn.wxReturn(out_trade_no, totalPrice.intValue());
							String out_refund_no = returnJson.getString("out_refund_no");
							String return_code = returnJson.getString("return_code");
							String result_code = returnJson.getString("result_code");
							if(return_code.equals("SUCCESS") && result_code.equals("SUCCESS")) {
								order.setPtrefundcode(out_refund_no);
								order.setOrderstatus(PtOrder.ORDERSTATUS_RETURN);
							}else {
								order.setPtrefundcode(out_refund_no);
								order.setOrderstatus(PtOrder.ORDERSTATUS_FAIL);
							}
							ptOrderDao.saveAndFlush(order);
						}
					}
					//回滚库存
					int ptgoodsNumber = ptGoods.getPtgoodsNumber();
					ptGoods.setPtgoodsNumber(ptgoodsNumber + number);
					ptGoodsDao.saveAndFlush(ptGoods);
				}
			}
		} catch (Exception e) {
			log.error("定时异常{}", e);
			throw AMPException.getException("定时任务异常!");
		}

	}
}
