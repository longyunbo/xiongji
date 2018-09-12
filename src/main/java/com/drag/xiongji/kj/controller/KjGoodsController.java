package com.drag.xiongji.kj.controller;

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

import com.drag.xiongji.kj.form.KjGoodsForm;
import com.drag.xiongji.kj.resp.KjGoodsResp;
import com.drag.xiongji.kj.service.KjGoodsService;
import com.drag.xiongji.kj.vo.KjGoodsDetailVo;
import com.drag.xiongji.kj.vo.KjGoodsVo;


@RestController
@RequestMapping(value = "/xiongji/kjgoods", produces = "application/json;charset=utf-8")
public class KjGoodsController {
	
	private final static Logger log = LoggerFactory.getLogger(KjGoodsController.class);

	@Autowired
	private KjGoodsService kjGoodsService;
	
	/**
	 * 查询所有的砍价商品
	 * @return
	 */
	@RequestMapping(value = "/list", method = {RequestMethod.POST,RequestMethod.GET})
	public @ResponseBody ResponseEntity<List<KjGoodsVo>> listGoods() {
		List<KjGoodsVo> rows= kjGoodsService.listGoods();
		return new ResponseEntity<List<KjGoodsVo>>(rows, HttpStatus.OK);
	}
	
	/**
	 * 查询砍价详情(查询所有发起砍价的用户)
	 * @param goodsId
	 * @return
	 */
	@RequestMapping(value = "/detail", method = {RequestMethod.POST,RequestMethod.GET})
	public @ResponseBody ResponseEntity<KjGoodsDetailVo> detail(@RequestParam(required = true) int goodsId) {
		KjGoodsDetailVo detailVo = kjGoodsService.goodsDetail(goodsId);
		return new ResponseEntity<KjGoodsDetailVo>(detailVo, HttpStatus.OK);
	}
	
	/**
	 * 查询砍价活动是否结束
	 * @return
	 */
	@RequestMapping(value = "/checkend", method = {RequestMethod.POST,RequestMethod.GET})
	public @ResponseBody ResponseEntity<Boolean> checkEnd(@RequestParam(required = true) int goodsId) {
		Boolean endFlag = kjGoodsService.checkEnd(goodsId);
		return new ResponseEntity<Boolean>(endFlag, HttpStatus.OK);
	}
	
	
	/**
	 * 本人发起砍价
	 * @return
	 */
	@RequestMapping(value = "/collage", method = {RequestMethod.POST,RequestMethod.GET})
	public @ResponseBody ResponseEntity<KjGoodsResp> collage(@RequestBody KjGoodsForm form) {
		KjGoodsResp br = kjGoodsService.collage(form);
		return new ResponseEntity<KjGoodsResp>(br, HttpStatus.OK);
	}
	
	/**
	 * 本人(好友)查询砍价详情
	 * @param goodsId
	 * @return
	 */
	@RequestMapping(value = "/mydetail", method = {RequestMethod.POST,RequestMethod.GET})
	public @ResponseBody ResponseEntity<KjGoodsDetailVo> mydetail(@RequestParam(required = true) String kjcode) {
		KjGoodsDetailVo detailVo = kjGoodsService.myDetail(kjcode);
		return new ResponseEntity<KjGoodsDetailVo>(detailVo, HttpStatus.OK);
	}
	
	/**
	 * 好友砍价
	 * @param form
	 * @return
	 */
	@RequestMapping(value = "/freindcollage", method = {RequestMethod.POST,RequestMethod.GET})
	public @ResponseBody ResponseEntity<KjGoodsResp> freindcollage(@RequestBody KjGoodsForm form) {
		KjGoodsResp br =  null;
		for(int i = 0 ; i<= 100 ; i++) {
			br = kjGoodsService.friendcollage(form);
		}
		return new ResponseEntity<KjGoodsResp>(br, HttpStatus.OK);
	}
	
	
}
