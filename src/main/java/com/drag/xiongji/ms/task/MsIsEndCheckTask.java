package com.drag.xiongji.ms.task;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.drag.xiongji.common.Constant;
import com.drag.xiongji.ms.dao.MsGoodsDao;
import com.drag.xiongji.ms.dao.MsRemindDao;
import com.drag.xiongji.ms.entity.MsGoods;
import com.drag.xiongji.ms.entity.MsRemind;
import com.drag.xiongji.utils.WxUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 定时任务查询活动是否结束，每分钟跑一次
 * @author longyunbo
 *
 */
@Slf4j
@Component
public class MsIsEndCheckTask {

	@Autowired
	MsGoodsDao msGoodsDao;
	@Autowired
	MsRemindDao msRemindDao;
	@Value("${weixin.url.msremind.templateid}")
	private String templateid;

	@Scheduled(cron = "${jobs.isEndCheckTask.schedule}")
	public void find() {
		try {
			List<MsGoods> MsGoodsList = msGoodsDao.findByIsEnd(0);
			if(MsGoodsList != null && MsGoodsList.size() > 0) {
				for (MsGoods msGoods : MsGoodsList) {
					Date endTime = msGoods.getEndTime();
					Date nowTime = new Timestamp(System.currentTimeMillis());
					if(nowTime.after(endTime)) {
						msGoods.setIsEnd(1);
						msGoods.setUpdateTime(new Timestamp(System.currentTimeMillis()));
						msGoodsDao.saveAndFlush(msGoods);
						log.info("【秒杀定时任务处理成功】，更新数据{}", msGoods);
					}
				}
			}
		} catch (Exception e) {
			log.error("定时异常{}", e);
		}
	}
	
	@Scheduled(cron = "${jobs.qixi.schedule}")
	public void qixiActivity() {
		try {
			List<MsRemind> msRemindList =  msRemindDao.findByStatus(MsRemind.STATUS_YES);
			for(MsRemind ms : msRemindList) {
				JSONObject json = new JSONObject();
				//openid
				json.put("touser", ms.getOpenid());
				json.put("template_id", templateid);
				json.put("page", "pages/seckill/seckilldetail/seckilldetail?shopid=" + ms.getMsgoodsId());
				json.put("form_id", ms.getFormId());
				//商品名称
				JSONObject keyword1 = new JSONObject();
				keyword1.put("value", ms.getMsgoodsName());
				keyword1.put("color", "#000000");
				//温馨提示
				JSONObject keyword2 = new JSONObject();
				keyword2.put("value", "秒杀活动还有3分钟就要开始了!");
				keyword2.put("color", "#000000");
				
				JSONObject data = new JSONObject();
				data.put("keyword1", keyword1);
				data.put("keyword2", keyword2);
				json.put("data", data);
				boolean result =  WxUtil.sendTemplateMsg(json);
				if(result) {
					ms.setSendstatus(Constant.SENDSTATUS_SUCC);
				}else {
					ms.setSendstatus(Constant.SENDSTATUS_FAIL);
				}
				msRemindDao.saveAndFlush(ms);
			}
		} catch (Exception e) {
			log.error("定时异常{}", e);
		}
	}

}
