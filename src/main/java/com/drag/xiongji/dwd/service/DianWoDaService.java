package com.drag.xiongji.dwd.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.drag.xiongji.common.Constant;
import com.drag.xiongji.common.exception.AMPException;
import com.drag.xiongji.dwd.form.CallBackForm;
import com.drag.xiongji.dwd.util.SignUtil;
import com.drag.xiongji.utils.HttpsUtil;
import com.drag.xiongji.wm.dao.OrderInfoDao;
import com.drag.xiongji.wm.entity.OrderInfo;
import com.drag.xiongji.wm.form.OrderInfoForm;
import com.drag.xiongji.wm.resp.OrderResp;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DianWoDaService {
	
	@Autowired
	OrderInfoDao orderInfoDao;
	
	/**
	 * 派发订单
	 * @param form
	 * @return
	 */
	public OrderResp orderSend(OrderInfoForm form) {
		OrderResp resp = new OrderResp();
		try {
			log.info("【点我达派发订单传入参数】:form= {}",JSON.toJSONString(form));
			String url = "api/v3/order-send.json";
			Date now = new Date(System.currentTimeMillis());
			BigDecimal order_price = form.getPrice().multiply(new BigDecimal(100));
			
			JSONObject json = new JSONObject();
			//渠道订单编号
			json.put("order_original_id", form.getOrderid());
			//渠道订单创建时间戳
			json.put("order_create_time", now.getTime());
			//订单备注
			json.put("order_remark", form.getOrder_remark());
			//订单金额（分）
			json.put("order_price",order_price.intValue());
			//订单商品重量，单位：克 如果无，传0
			json.put("cargo_weight", 250);
			//商品份数，默认传1
			json.put("cargo_num", 1);
			//行政区划代码 如杭州：330100
			json.put("city_code", form.getCity_code());
			//商家编号
			json.put("seller_id", form.getSeller_id());
			//商家店铺名称
			json.put("seller_name", form.getSeller_name());
			//商家联系方式
			json.put("seller_mobile", form.getSeller_mobile());
			//商家文字地址
			json.put("seller_address", form.getSeller_address());
			//商家纬度坐标.(坐标系为高德地图坐标系，又称火星坐标). （单位：度）
			json.put("seller_lat", form.getSeller_lat());
			//商家经度坐标.(坐标系为高德地图坐标系，又称火星坐标) （单位：度）
			json.put("seller_lng", form.getSeller_lng());
			//收货人姓名
			json.put("consignee_name", form.getReceiptName());
			//收货人手机号码
			json.put("consignee_mobile", form.getReceiptTel());
			//收货人地址
			json.put("consignee_address", form.getReceiptAddress());
			//收货人纬度坐标.(坐标系为高德地图坐标系，又称火星坐标)
			json.put("consignee_lat", form.getConsignee_lat());
			//收货人经度坐标.(坐标系为高德地图坐标系，又称火星坐标)
			json.put("consignee_lng", form.getConsignee_lng());
			//配送员到店是否需要垫付订单金额	1：是 0：否
			json.put("money_rider_needpaid", 0);
			//配送员到店后先行垫付的金额(分)，一般货到付款情况下等于餐品价格。若无，传0
			json.put("money_rider_prepaid", 0);
			//配送员送达到客户时，向客户收取的费用（分）
			json.put("money_rider_charge", 0);
			//商品必须到店才开始准备或是排队购买的情况下，在商家处等待所需时间（秒）
			json.put("time_waiting_at_seller", 0);
			//渠道支付配送费（分）
			json.put("delivery_fee_from_seller", 0);
			  
			Map<String, Object> params =JSONObject.parseObject(json.toString());
			String commParam = SignUtil.getComParam(url,params);
			log.info("【点我达派发订单传入参数json】:{}",json);
			String httpresult = HttpsUtil.sendPost(commParam, params);
			log.info("【点我达派发订单返回参数httpresult】:{}",httpresult);
			if (httpresult != null) {
				JSONObject jsonResult = JSON.parseObject(httpresult);
				String errorCode = jsonResult.getString("errorCode");
				String message = jsonResult.getString("message");
				if("0".equals(errorCode)) {
					String result = jsonResult.getString("result");
					log.info("【点我达派发订单号】result:{}",result);
				}else {
					resp.setReturnCode(Constant.FAIL);
					resp.setErrorMessage(message);
					return resp;
				}
			}else {
				resp.setReturnCode(Constant.FAIL);
				resp.setErrorMessage("点我达派发订单失败!");
				return resp;
			}
		} catch (Exception e) {
			log.error("【点我达派发订单异常】{}",e);
			throw AMPException.getException("系统异常!");
		}
		resp.setReturnCode(Constant.SUCCESS);
		resp.setErrorMessage("点我达派发订单成功!");
		return resp;
	}
	
	/**
	 * 获取订单
	 * @param order_original_id
	 * @return
	 */
	public JSONObject orderGet(String order_original_id) {
		JSONObject resp = new JSONObject();
		try {
			log.info("【点我达查询订单传入参数】:order_original_id= {}",order_original_id);
			String url = "api/v3/order-get.json";
			JSONObject json = new JSONObject();
			//渠道订单编号
			json.put("order_original_id", order_original_id);
			
			Map<String, Object> params =JSONObject.parseObject(json.toString());
			String commParam = SignUtil.getComParam(url,params);
			log.info("【点我达查询订单传入参数json】:{}",json);
			String httpresult = HttpsUtil.sendPost(commParam, params);
			log.info("【点我达查询订单返回参数httpresult】:{}",httpresult);
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
			log.error("【点我达查询订单异常】{}",e);
			throw AMPException.getException("系统异常!");
		}
		return resp;
	}
	
	
	
	/**
	 * 取消订单
	 * @param order_original_id
	 * @param cancle_reason
	 * @return
	 */
	public JSONObject orderCancel(String order_original_id,String cancle_reason) {
		JSONObject resp = new JSONObject();
		try {
			log.info("【点我达取消订单传入参数】:order_original_id= {}",order_original_id);
			String url = "api/v3/order-cancel.json";
			JSONObject json = new JSONObject();
			//渠道订单编号
			json.put("order_original_id", order_original_id);
			json.put("cancle_reason", cancle_reason);
			
			Map<String, Object> params =JSONObject.parseObject(json.toString());
			String commParam = SignUtil.getComParam(url,params);
			log.info("【点我达取消订单传入参数json】:{}",json);
			String httpresult = HttpsUtil.sendPost(commParam, params);
			log.info("【点我达取消订单返回参数httpresult】:{}",httpresult);
			if (httpresult != null) {
				JSONObject jsonResult = JSON.parseObject(httpresult);
				resp.put("errorCode", jsonResult.get("errorCode"));
				resp.put("message", jsonResult.get("message"));
			}
		} catch (Exception e) {
			log.error("【点我达取消订单异常】{}",e);
			throw AMPException.getException("系统异常!");
		}
		return resp;
	}
	
	/**
	 * 点我达查询配送员信息
	 * @param order_original_id
	 * @return
	 */
	public JSONObject orderRiderPosition(String order_original_id) {
		JSONObject resp = new JSONObject();
		try {
			log.info("【点我达查询配送员传入参数】:order_original_id= {}",order_original_id);
			String url = "api/v3/order-rider-position.json";
			JSONObject json = new JSONObject();
			//渠道订单编号
			json.put("order_original_id", order_original_id);
			
			Map<String, Object> params =JSONObject.parseObject(json.toString());
			String commParam = SignUtil.getComParam(url,params);
			log.info("【点我达查询配送员传入参数json】:{}",json);
			String httpresult = HttpsUtil.sendPost(commParam, params);
			log.info("【点我达查询配送员返回参数httpresult】:{}",httpresult);
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
			log.error("【点我达查询配送员异常】{}",e);
			throw AMPException.getException("系统异常!");
		}
		return resp;
	}
	
	/**
	 * 订单回调函数
	 * @param form
	 * @return
	 */
	public JSONObject callback(CallBackForm form) {
		JSONObject resp = new JSONObject();
		try {
			log.info("【点我达订单回调传入参数】:form= {}",JSONObject.toJSONString(form));
			String order_original_id = form.getOrder_original_id();
			int order_status = form.getOrder_status();
			Long time_status_update = form.getTime_status_update();
			String cancel_reason = form.getCancel_reason();
			OrderInfo order = orderInfoDao.findByOrderId(order_original_id);
			if(order != null) {
				order.setDeliverystatus(order_status);
				order.setUpdateTime(new Date(time_status_update));
				order.setRemark(cancel_reason);
				orderInfoDao.saveAndFlush(order);
				resp.put("success", true);
			}
		} catch (Exception e) {
			log.error("【点我达订单回调异常】{}",e);
			throw AMPException.getException("系统异常!");
		}
		return resp;
	}
	
	
}
