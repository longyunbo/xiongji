package com.drag.xiongji.dwd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.drag.xiongji.dwd.form.CallBackForm;
import com.drag.xiongji.dwd.service.DianWoDaService;
import com.drag.xiongji.dwd.service.DianWoDaServiceTest;
import com.drag.xiongji.wm.form.OrderInfoForm;
import com.drag.xiongji.wm.resp.OrderResp;


@RestController
@RequestMapping(value = "/xiongji/dwd")
public class DianWoDaController {
	
	@Autowired
	DianWoDaService dianWoDaService;
	@Autowired
	DianWoDaServiceTest dianWoDaServiceTest;
	
	@RequestMapping(value = "/ordersend", method = {RequestMethod.POST,RequestMethod.GET})
	public @ResponseBody ResponseEntity<OrderResp> orderSend() {
		OrderInfoForm form = new OrderInfoForm();
		OrderResp resp = dianWoDaService.orderSend(form);
		return new ResponseEntity<OrderResp>(resp, HttpStatus.OK);
	}
	
	/**
	 * 获取订单信息
	 * @param order_original_id
	 * @return
	 */
	@RequestMapping(value = "/orderget", method = {RequestMethod.POST,RequestMethod.GET})
	public @ResponseBody ResponseEntity<JSONObject> orderGet(@RequestParam String order_original_id) {
		JSONObject Json = dianWoDaService.orderGet(order_original_id);
		return new ResponseEntity<JSONObject>(Json, HttpStatus.OK);
	}
	
	/**
	 * 取消订单
	 * @param order_original_id
	 * @param cancle_reason
	 * @return
	 */
	@RequestMapping(value = "/ordercancel", method = {RequestMethod.POST,RequestMethod.GET})
	public @ResponseBody ResponseEntity<JSONObject> orderCancel(@RequestParam String order_original_id,@RequestParam String cancle_reason) {
		JSONObject Json = dianWoDaService.orderCancel(order_original_id,cancle_reason);
		return new ResponseEntity<JSONObject>(Json, HttpStatus.OK);
	}
	
	/**
	 * 点我达回调函数
	 * @param form
	 * @return
	 */
	@RequestMapping(value = "/formalcallback", method = {RequestMethod.POST,RequestMethod.GET})
//	@RequestMapping(value = "/callback", method = {RequestMethod.POST,RequestMethod.GET})
	public @ResponseBody ResponseEntity<JSONObject> callback(@RequestBody CallBackForm form) {
		JSONObject Json = dianWoDaService.callback(form);
		return new ResponseEntity<JSONObject>(Json, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/ordertest", method = {RequestMethod.POST,RequestMethod.GET})
	public @ResponseBody ResponseEntity<JSONObject> orderTest(@RequestParam String order_original_id,@RequestParam String type) {
		JSONObject Json = dianWoDaServiceTest.acceptedTest(order_original_id,type);
		return new ResponseEntity<JSONObject>(Json, HttpStatus.OK);
	}
	
}
