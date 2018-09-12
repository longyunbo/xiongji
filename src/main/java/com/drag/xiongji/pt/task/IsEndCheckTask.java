package com.drag.xiongji.pt.task;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
import com.drag.xiongji.user.entity.User;
import com.drag.xiongji.utils.StringUtil;
import com.drag.xiongji.utils.WxUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 定时任务查询活动是否结束，每分钟跑一次
 * @author longyunbo
 *
 */
@Slf4j
@Component
public class IsEndCheckTask {

	@Autowired
	PtGoodsDao ptGoodsDao;
	@Autowired
	PtUserDao ptUserDao;
	@Autowired
	PtOrderDao ptOrderDao;

	@Scheduled(cron = "${jobs.isEndCheckTask.schedule}")
	@Transactional
	public void find() {
		try {
			Date nowTime = new Timestamp(System.currentTimeMillis());
			List<PtGoods> ptGoodsList = ptGoodsDao.findByIsEnd(0);
			if(ptGoodsList != null && ptGoodsList.size() > 0) {
				for (PtGoods ptGoods : ptGoodsList) {
					Date endTime = ptGoods.getEndTime();
					if(nowTime.after(endTime)) {
						ptGoods.setIsEnd(1);
						ptGoods.setUpdateTime(new Timestamp(System.currentTimeMillis()));
						ptGoodsDao.saveAndFlush(ptGoods);
						
						
						int goodsId = ptGoods.getPtgoodsId();
						//查询拼团中的人数
						List<PtUser> ptList = ptUserDao.findByPtGoodsIdAndStatus(goodsId,PtUser.PTSTATUS_MIDDLE);
						Set<String> ptcodes = new HashSet<String>();
						if(ptList != null && ptList.size() > 0) {
							for(PtUser ku : ptList) {
								//把在拼团中的状态的修改成失败
								ku.setPtstatus(PtUser.PTSTATUS_FAIL);
								ptUserDao.saveAndFlush(ku);
								ptcodes.add(ku.getPtcode());
							}
						}
						
						if(ptcodes != null && ptcodes.size() > 0) {
							//拼团的数量 
							int allNumber = 0;
							List<PtOrder> orders = ptOrderDao.findByPtCodeIn(ptcodes);
							
							for(PtOrder order : orders) {
								int number = order.getNumber();
								allNumber = allNumber + number;
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
							ptGoods.setPtgoodsNumber(ptgoodsNumber + allNumber);
							ptGoodsDao.saveAndFlush(ptGoods);
						}
						log.info("【拼团定时任务处理成功】，更新数据{}", ptGoods);
					}
				}
			}
		} catch (Exception e) {
			log.error("定时异常{}", e);
			throw AMPException.getException("定时任务异常!");
		}

	}

}
