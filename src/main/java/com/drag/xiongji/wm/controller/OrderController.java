package com.drag.xiongji.wm.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.drag.xiongji.common.BaseResponse;
import com.drag.xiongji.wm.form.OrderCommentForm;
import com.drag.xiongji.wm.form.OrderInfoForm;
import com.drag.xiongji.wm.resp.OrderResp;
import com.drag.xiongji.wm.service.OrderService;
import com.drag.xiongji.wm.vo.OrderCommentVo;
import com.drag.xiongji.wm.vo.OrderDetailVo;
import com.drag.xiongji.wm.vo.OrderInfoVo;



@RestController
@RequestMapping(value = "/xiongji/order")
public class OrderController {

	@Autowired
	private OrderService orderInfoService;
	
	/**
	 * 外卖购买下单
	 * @param form
	 * @return
	 */
	@RequestMapping(value = "/purchase", method = {RequestMethod.POST,RequestMethod.GET})
	public @ResponseBody ResponseEntity<OrderResp> purchase(@RequestBody OrderInfoForm form) {
		OrderResp detailVo = orderInfoService.purchase(form);
		return new ResponseEntity<OrderResp>(detailVo, HttpStatus.OK);
	}
	
	
	/**
	 * 获取我的订单
	 * @param openid
	 * @return
	 */
	@RequestMapping(value = "/myorders", method = {RequestMethod.POST,RequestMethod.GET})
	public @ResponseBody ResponseEntity<List<OrderInfoVo>> myorders(@RequestParam(required = true)  String openid,@RequestParam String type) {
		List<OrderInfoVo> list = orderInfoService.myOrders(openid,type);
		return new ResponseEntity<List<OrderInfoVo>>(list, HttpStatus.OK);
	}
	
	/**
	 * 订单详情
	 * @param orderid
	 * @return
	 */
	@RequestMapping(value = "/orderdetail", method = {RequestMethod.POST,RequestMethod.GET})
	public @ResponseBody ResponseEntity<List<OrderDetailVo>> orderDetail(@RequestParam(required = true)  String orderid) {
		List<OrderDetailVo> list = orderInfoService.orderDetail(orderid);
		return new ResponseEntity<List<OrderDetailVo>>(list, HttpStatus.OK);
	}
	
	/**
	 * 申请退款
	 * @param orderid
	 * @return
	 */
	@RequestMapping(value = "/applyreturn", method = {RequestMethod.POST,RequestMethod.GET})
	public @ResponseBody ResponseEntity<BaseResponse> applyReturn(@RequestParam(required = true)  String orderid) {
		BaseResponse resp = orderInfoService.applyReturn(orderid);
		return new ResponseEntity<BaseResponse>(resp, HttpStatus.OK);
	}
	
	/**
	 * 外卖新增评论
	 * @param form
	 * @return
	 */
	@RequestMapping(value = "/comment", method = {RequestMethod.POST,RequestMethod.GET})
	public @ResponseBody ResponseEntity<OrderResp> comment(@RequestBody OrderCommentForm form) {
		OrderResp detailVo = orderInfoService.comment(form);
		return new ResponseEntity<OrderResp>(detailVo, HttpStatus.OK);
	}
	
	/**
	 * 商品评价集合
	 * @param goodsId
	 * @return
	 */
	@RequestMapping(value = "/commentlist", method = {RequestMethod.POST,RequestMethod.GET})
	public @ResponseBody ResponseEntity<List<OrderCommentVo>> orderDetail(@RequestParam(required = true)  int goodsId) {
		List<OrderCommentVo> list = orderInfoService.goodsComment(goodsId);
		return new ResponseEntity<List<OrderCommentVo>>(list, HttpStatus.OK);
	}
	
	
	/**
	 * 用户上传图片
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/picture", method = {RequestMethod.POST,RequestMethod.GET})
    public void uploadPicture(HttpServletRequest request, HttpServletResponse response) throws Exception {
		orderInfoService.uploadPicture(request, response);
	}
}
