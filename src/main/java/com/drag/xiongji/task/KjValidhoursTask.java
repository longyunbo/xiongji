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
import com.drag.xiongji.kj.dao.KjGoodsDao;
import com.drag.xiongji.kj.dao.KjUserDao;
import com.drag.xiongji.kj.entity.KjGoods;
import com.drag.xiongji.kj.entity.KjUser;
import com.drag.xiongji.zl.entity.ZlUser;

import lombok.extern.slf4j.Slf4j;
/**
 * 定时任务查询砍价有效期
 * @author longyunbo
 *
 */
@Slf4j
@Component
public class KjValidhoursTask {
	
	@Autowired
	KjGoodsDao kjGoodsDao;
	@Autowired
	KjUserDao kjUserDao;
	
	@Scheduled(cron = "${jobs.hoursCheckTask.schedule}")
	@Transactional
	public void find() {
		
		try {
			Date nowTime = new Timestamp(System.currentTimeMillis());
			List<KjGoods> kjGoodsList = kjGoodsDao.findByIsEnd(0);
			for (KjGoods kjGoods : kjGoodsList) {
				//拼团有效时间，默认为24小时
				int kjValidhours = kjGoods.getKjValidhours();
				int goodsId = kjGoods.getKjgoodsId();
				//根据商品编号查询出砍价中的用户
				List<KjUser> userList = kjUserDao.findByKjGoodsIdAndKjstatusAndIsHeader(goodsId, KjUser.PTSTATUS_MIDDLE,KjUser.ISHEADER_YES);
				Set<String> kjcodes = new HashSet<String>();
				//砍价的默认数量为1个 
				int number = 0;
				if(userList != null && userList.size() > 0) {
					for(KjUser user : userList) {
						Date createTime =  user.getCreateTime();
						long compareDate = (nowTime.getTime() - createTime.getTime()) / (60*60*1000);
						if(compareDate >= kjValidhours) {
							//修改为砍价失败
							kjcodes.add(user.getKjcode());
						}
					}
					if(kjcodes != null && kjcodes.size() >0) {
						List<KjUser> childList =  kjUserDao.findByKjCodeIn(kjcodes);
						if(childList != null && childList.size() > 0) {
							for(KjUser user : childList) {
								//修改为砍价失败
								user.setKjstatus(ZlUser.PTSTATUS_FAIL);
								kjUserDao.saveAndFlush(user);
								if(KjUser.ISHEADER_YES == user.getIsHeader()) {
									number ++ ;
								}
							}
						}
						//回滚库存
						int kjgoodsNumber = kjGoods.getKjgoodsNumber();
						kjGoods.setKjgoodsNumber(kjgoodsNumber + number);
						kjGoodsDao.saveAndFlush(kjGoods);
					}
				}
			}
		} catch (Exception e) {
			log.error("定时异常{}", e);
			throw AMPException.getException("定时任务异常!");
		}

	}
}
