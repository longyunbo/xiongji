package com.drag.xiongji.pt.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.drag.xiongji.pt.form.PtGoodsForm;
import com.drag.xiongji.pt.resp.PtGoodsResp;
import com.drag.xiongji.pt.service.PtGoodsService;
import com.drag.xiongji.pt.vo.PtGoodsDetailVo;
import com.drag.xiongji.pt.vo.PtGoodsVo;
import com.drag.xiongji.utils.WxUtil;


@RestController
@RequestMapping(value = "/xiongji/ptgoods", produces = "application/json;charset=utf-8")
public class PtGoodsController {
	
	private final static Logger log = LoggerFactory.getLogger(PtGoodsController.class);

	@Autowired
	private PtGoodsService ptGoodsService;
	
	/**
	 * 查询所有的拼团商品
	 * @return
	 */
	@RequestMapping(value = "/list", method = {RequestMethod.POST,RequestMethod.GET})
	public @ResponseBody ResponseEntity<List<PtGoodsVo>> listGoods() {
		List<PtGoodsVo> rows= ptGoodsService.listGoods();
		return new ResponseEntity<List<PtGoodsVo>>(rows, HttpStatus.OK);
	}
	
	/**
	 * 查询拼团详情(查询所有发起拼团的用户)
	 * @param goodsId
	 * @return
	 */
	@RequestMapping(value = "/detail", method = {RequestMethod.POST,RequestMethod.GET})
	public @ResponseBody ResponseEntity<PtGoodsDetailVo> detail(@RequestParam(required = true) int goodsId) {
		PtGoodsDetailVo detailVo = ptGoodsService.goodsDetail(goodsId);
		return new ResponseEntity<PtGoodsDetailVo>(detailVo, HttpStatus.OK);
	}
	
	/**
	 * 查询拼团活动是否结束
	 * @return
	 */
	@RequestMapping(value = "/checkend", method = {RequestMethod.POST,RequestMethod.GET})
	public @ResponseBody ResponseEntity<Boolean> checkEnd(@RequestParam(required = true) int goodsId) {
		Boolean endFlag = ptGoodsService.checkEnd(goodsId);
		return new ResponseEntity<Boolean>(endFlag, HttpStatus.OK);
	}
	
	
	/**
	 * 本人发起拼团
	 * @return
	 */
	@RequestMapping(value = "/collage", method = {RequestMethod.POST,RequestMethod.GET})
	public @ResponseBody ResponseEntity<PtGoodsResp> collage(@RequestBody PtGoodsForm form) {
		PtGoodsResp br = ptGoodsService.collage(form);
		return new ResponseEntity<PtGoodsResp>(br, HttpStatus.OK);
	}
	
	/**
	 * 本人(好友)查询拼团详情
	 * @param goodsId
	 * @return
	 */
	@RequestMapping(value = "/mydetail", method = {RequestMethod.POST,RequestMethod.GET})
	public @ResponseBody ResponseEntity<PtGoodsDetailVo> mydetail(@RequestParam(required = true) String ptcode) {
		PtGoodsDetailVo detailVo = ptGoodsService.myDetail(ptcode);
		return new ResponseEntity<PtGoodsDetailVo>(detailVo, HttpStatus.OK);
	}
	
	/**
	 * 好友拼团
	 * @param form
	 * @return
	 */
	@RequestMapping(value = "/freindcollage", method = {RequestMethod.POST,RequestMethod.GET})
	public @ResponseBody ResponseEntity<PtGoodsResp> freindcollage(@RequestBody PtGoodsForm form) {
		PtGoodsResp br = ptGoodsService.friendcollage(form);
//		ptGoodsService.updatePtstatus(form);
		return new ResponseEntity<PtGoodsResp>(br, HttpStatus.OK);
	}
	
	/**
	 * 生成海报
	 * @param code
	 * @param page
	 * @return
	 */
	@RequestMapping(value = "/poster", method = {RequestMethod.POST,RequestMethod.GET})
	public @ResponseBody ResponseEntity<String> poster(HttpServletRequest request,HttpServletResponse response) {
		WxUtil.getPoster(request,response);
		return new ResponseEntity<String>("OK", HttpStatus.OK);
	}
	
}
