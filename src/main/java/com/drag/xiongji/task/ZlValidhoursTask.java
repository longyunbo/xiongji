package com.drag.xiongji.task;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.drag.xiongji.common.exception.AMPException;
import com.drag.xiongji.pt.entity.PtUser;
import com.drag.xiongji.zl.dao.ZlGoodsDao;
import com.drag.xiongji.zl.dao.ZlUserDao;
import com.drag.xiongji.zl.entity.ZlGoods;
import com.drag.xiongji.zl.entity.ZlUser;

import lombok.extern.slf4j.Slf4j;
/**
 * 定时任务查询助力有效期
 * @author longyunbo
 *
 */
@Slf4j
@Component
public class ZlValidhoursTask {
	
	@Autowired
	ZlGoodsDao zlGoodsDao;
	@Autowired
	ZlUserDao zlUserDao;
	
	@Scheduled(cron = "${jobs.hoursCheckTask.schedule}")
	@Transactional
	public void find() {
		
		try {
			Date nowTime = new Timestamp(System.currentTimeMillis());
			List<ZlGoods> zlGoodsList = zlGoodsDao.findByIsEnd(0);
			for (ZlGoods zlGoods : zlGoodsList) {
				//拼团有效时间，默认为24小时
				int zlValidhours = zlGoods.getZlValidhours();
				int goodsId = zlGoods.getZlgoodsId();
				//根据商品编号查询出砍价中的团长用户
				List<ZlUser> userList = zlUserDao.findByZlGoodsIdAndZlstatusAndIsHeader(goodsId, ZlUser.PTSTATUS_MIDDLE,ZlUser.ISHEADER_YES);
				Set<String> zlcodes = new HashSet<String>();
				//助力的默认数量为1个 
				int number = 0;
				if(userList != null && userList.size() > 0) {
					for(ZlUser user : userList) {
						Date createTime =  user.getCreateTime();
						long compareDate = (nowTime.getTime() - createTime.getTime()) / (60*60*1000);
						if(compareDate >= zlValidhours) {
							//修改为砍价失败
							zlcodes.add(user.getZlcode());
						}
					}
					if(zlcodes != null && zlcodes.size() > 0) {
						List<ZlUser> childList =  zlUserDao.findByZlCodeIn(zlcodes);
						if(childList != null && childList.size() > 0) {
							for(ZlUser user : childList) {
								//修改为砍价失败
								user.setZlstatus(ZlUser.PTSTATUS_FAIL);
								zlUserDao.saveAndFlush(user);
								if(ZlUser.ISHEADER_YES == user.getIsHeader()) {
									number ++ ;
								}
							}
						}
						//回滚库存
						int zlgoodsNumber = zlGoods.getZlgoodsNumber();
						zlGoods.setZlgoodsNumber(zlgoodsNumber + number);
						zlGoodsDao.saveAndFlush(zlGoods);
					}
				}
			}
		} catch (Exception e) {
			log.error("定时异常{}", e);
			throw AMPException.getException("定时任务异常!");
		}

	}
}
