package com.drag.xiongji.task;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.drag.xiongji.common.exception.AMPException;
import com.drag.xiongji.user.dao.UserTicketDao;
import com.drag.xiongji.user.entity.UserTicket;

import lombok.extern.slf4j.Slf4j;
/**
 * 卡券过期定时处理
 * @author longyunbo
 *
 */
@Slf4j
@Component
public class TicketValidhoursTask {
	
	@Autowired
	UserTicketDao userTicketDao;
	
	@Scheduled(cron = "${jobs.hoursCheckTask.schedule}")
	@Transactional
	public void find() {
		
		try {
			Date nowTime = new Timestamp(System.currentTimeMillis());
			//查询未使用的卡券
			List<UserTicket> ticketList = userTicketDao.findByStatus(UserTicket.STATUS_NO);
			for (UserTicket ticket : ticketList) {
				//卡券有效时间，默认为7天
				int team = ticket.getTerm();
				Date createTime =  ticket.getCreateTime();
				long compareDate = (nowTime.getTime() - createTime.getTime()) / (60*60*1000*24);
				if(compareDate >= team) {
					//设为过期
					ticket.setStatus(UserTicket.STATUS_OVER);
					userTicketDao.saveAndFlush(ticket);
				}
			}
		} catch (Exception e) {
			log.error("定时异常{}", e);
			throw AMPException.getException("定时任务异常!");
		}

	}
}
