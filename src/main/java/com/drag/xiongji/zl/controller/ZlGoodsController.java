package com.drag.xiongji.zl.controller;

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

import com.drag.xiongji.zl.form.ZlGoodsForm;
import com.drag.xiongji.zl.resp.ZlGoodsResp;
import com.drag.xiongji.zl.service.ZlGoodsService;
import com.drag.xiongji.zl.vo.ZlGoodsDetailVo;
import com.drag.xiongji.zl.vo.ZlGoodsVo;


@RestController
@RequestMapping(value = "/xiongji/zlgoods", produces = "application/json;charset=utf-8")
public class ZlGoodsController {
	
	private final static Logger log = LoggerFactory.getLogger(ZlGoodsController.class);

	@Autowired
	private ZlGoodsService zlGoodsService;
	
	/**
	 * 查询所有的助力商品
	 * @return
	 */
	@RequestMapping(value = "/list", method = {RequestMethod.POST,RequestMethod.GET})
	public @ResponseBody ResponseEntity<List<ZlGoodsVo>> listGoods() {
		List<ZlGoodsVo> rows= zlGoodsService.listGoods();
		return new ResponseEntity<List<ZlGoodsVo>>(rows, HttpStatus.OK);
	}
	
	/**
	 * 查询助力详情(查询所有发起助力的用户)
	 * @param goodsId
	 * @return
	 */
	@RequestMapping(value = "/detail", method = {RequestMethod.POST,RequestMethod.GET})
	public @ResponseBody ResponseEntity<ZlGoodsDetailVo> detail(@RequestParam(required = true) int goodsId) {
		ZlGoodsDetailVo detailVo = zlGoodsService.goodsDetail(goodsId);
		return new ResponseEntity<ZlGoodsDetailVo>(detailVo, HttpStatus.OK);
	}
	
	/**
	 * 查询助力活动是否结束
	 * @return
	 */
	@RequestMapping(value = "/checkend", method = {RequestMethod.POST,RequestMethod.GET})
	public @ResponseBody ResponseEntity<Boolean> checkEnd(@RequestParam(required = true) int goodsId) {
		Boolean endFlag = zlGoodsService.checkEnd(goodsId);
		return new ResponseEntity<Boolean>(endFlag, HttpStatus.OK);
	}
	
	
	/**
	 * 本人发起助力
	 * @return
	 */
	@RequestMapping(value = "/collage", method = {RequestMethod.POST,RequestMethod.GET})
	public @ResponseBody ResponseEntity<ZlGoodsResp> collage(@RequestBody ZlGoodsForm form) {
		ZlGoodsResp br = zlGoodsService.collage(form);
		return new ResponseEntity<ZlGoodsResp>(br, HttpStatus.OK);
	}
	
	/**
	 * 本人(好友)查询助力详情
	 * @param goodsId
	 * @return
	 */
	@RequestMapping(value = "/mydetail", method = {RequestMethod.POST,RequestMethod.GET})
	public @ResponseBody ResponseEntity<ZlGoodsDetailVo> mydetail(@RequestParam(required = true) String zlcode) {
		ZlGoodsDetailVo detailVo = zlGoodsService.myDetail(zlcode);
		return new ResponseEntity<ZlGoodsDetailVo>(detailVo, HttpStatus.OK);
	}
	
	/**
	 * 好友助力
	 * @param form
	 * @return
	 */
	@RequestMapping(value = "/freindcollage", method = {RequestMethod.POST,RequestMethod.GET})
	public @ResponseBody ResponseEntity<ZlGoodsResp> freindcollage(@RequestBody ZlGoodsForm form) {
		ZlGoodsResp br = zlGoodsService.friendcollage(form);
		return new ResponseEntity<ZlGoodsResp>(br, HttpStatus.OK);
	}
	
	
}
