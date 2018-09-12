package com.drag.xiongji.ms.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.drag.xiongji.ms.form.MsGoodsForm;
import com.drag.xiongji.ms.resp.MsGoodsResp;
import com.drag.xiongji.ms.service.MsGoodsService;
import com.drag.xiongji.ms.vo.MsGoodsDetailVo;
import com.drag.xiongji.ms.vo.MsGoodsVo;
import com.drag.xiongji.ms.vo.MsRemindVo;


@RestController
@RequestMapping(value = "/xiongji/msgoods", produces = "application/json;charset=utf-8")
public class MsGoodsController {
	
	private final static Logger log = LoggerFactory.getLogger(MsGoodsController.class);

	@Autowired
	private MsGoodsService msGoodsService;
	
	/**
	 * 查询所有的秒杀商品
	 * @return
	 */
	@RequestMapping(value = "/list", method = {RequestMethod.POST,RequestMethod.GET})
	public @ResponseBody ResponseEntity<List<MsGoodsVo>> listGoods() {
		List<MsGoodsVo> rows= msGoodsService.listGoods();
		return new ResponseEntity<List<MsGoodsVo>>(rows, HttpStatus.OK);
	}
	
	/**
	 * 查询秒杀详情(查询所有发起秒杀的用户)
	 * @param goodsId
	 * @return
	 */
	@RequestMapping(value = "/detail", method = {RequestMethod.POST,RequestMethod.GET})
	public @ResponseBody ResponseEntity<MsGoodsDetailVo> detail(@RequestParam(required = true) int goodsId) {
		MsGoodsDetailVo detailVo = msGoodsService.goodsDetail(goodsId);
		return new ResponseEntity<MsGoodsDetailVo>(detailVo, HttpStatus.OK);
	}
	
	/**
	 * 查询秒杀活动是否结束
	 * @return
	 */
	@RequestMapping(value = "/checkend", method = {RequestMethod.POST,RequestMethod.GET})
	public @ResponseBody ResponseEntity<Boolean> checkEnd(@RequestParam(required = true) int goodsId) {
		Boolean endFlag = msGoodsService.checkEnd(goodsId);
		return new ResponseEntity<Boolean>(endFlag, HttpStatus.OK);
	}
	
	
	/**
	 * 发起秒杀
	 * @return
	 */
	@RequestMapping(value = "/collage", method = {RequestMethod.POST,RequestMethod.GET})
	public @ResponseBody ResponseEntity<MsGoodsResp> collage(@RequestBody MsGoodsForm form) {
		MsGoodsResp br = msGoodsService.collage(form);
		return new ResponseEntity<MsGoodsResp>(br, HttpStatus.OK);
	}
	
	/**
	 * 秒杀提醒
	 * @param form
	 * @return
	 */
	@RequestMapping(value = "/remind", method = {RequestMethod.POST,RequestMethod.GET})
	public @ResponseBody ResponseEntity<MsGoodsResp> remind(@RequestBody MsGoodsForm form) {
		MsGoodsResp br = msGoodsService.remind(form);
		return new ResponseEntity<MsGoodsResp>(br, HttpStatus.OK);
	}
	
	/**
	 * 查询秒杀提醒列表
	 * @param openid
	 * @return
	 */
	@RequestMapping(value = "/remindlist", method = {RequestMethod.POST,RequestMethod.GET})
	public @ResponseBody ResponseEntity<List<MsRemindVo>> remindList(@RequestParam String openid) {
		List<MsRemindVo> br = msGoodsService.remindList(openid);
		return new ResponseEntity<List<MsRemindVo>>(br, HttpStatus.OK);
	}
	
}
