package com.drag.xiongji.dwd.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.drag.xiongji.common.exception.AMPException;
import com.drag.xiongji.dwd.util.SignUtil;
import com.drag.xiongji.utils.HttpsUtil;
import com.drag.xiongji.wm.dao.OrderInfoDao;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DianWoDaServiceTest {
	
	@Autowired
	OrderInfoDao orderInfoDao;
	
	
	/**
	 * 获取订单
	 * @param order_original_id
	 * @return
	 */
	public JSONObject acceptedTest(String order_original_id,String type) {
		JSONObject resp = new JSONObject();
		try {
			log.info("【点我达订单骑手传入参数】:order_original_id= {},type={}",order_original_id,type);
			String url = "";
			if(type.equals("js")) {
				url = "api/v3/order-accepted-test.json";
			}else if(type.equals("dd")) {
				url = "api/v3/order-arrive-test.json";
			}
			else if(type.equals("qh")) {
				url = "api/v3/order-fetch-test.json";
			}else {
				url = "api/v3/order-finish-test.json";
			}
			JSONObject json = new JSONObject();
			//渠道订单编号
			json.put("order_original_id", order_original_id);
			
			Map<String, Object> params =JSONObject.parseObject(json.toString());
			String commParam = SignUtil.getComParam(url,params);
			log.info("【点我达订单骑手传入参数commParam】:{}",commParam);
			String httpresult = HttpsUtil.sendPost(commParam, params);
			log.info("【点我达订单骑手返回参数httpresult】:{}",httpresult);
			if (httpresult != null) {
				JSONObject jsonResult = JSON.parseObject(httpresult);
				String errorCode = jsonResult.getString("errorCode");
				if("0".equals(errorCode)) {
					resp = (JSONObject) jsonResult.get("result");
				}else {
					resp.put("errorCode", jsonResult.get("errorCode"));
					resp.put("message", jsonResult.get("message"));
				}
			}
		} catch (Exception e) {
			log.error("【点我达订单骑手异常】{}",e);
			throw AMPException.getException("系统异常!");
		}
		return resp;
	}

	
}
