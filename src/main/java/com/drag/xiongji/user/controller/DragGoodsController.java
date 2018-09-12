package com.drag.xiongji.user.controller;

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

import com.drag.xiongji.common.BaseResponse;
import com.drag.xiongji.user.form.DragBoneForm;
import com.drag.xiongji.user.service.DragGoodsService;
import com.drag.xiongji.user.vo.DragGoodsVo;
import com.drag.xiongji.user.vo.UserDragUsedRecordVo;
import com.drag.xiongji.user.vo.UserTicketTemplateVo;




@RestController
@RequestMapping(value = "/xiongji/draggoods")
public class DragGoodsController {
	
	private final static Logger log = LoggerFactory.getLogger(DragGoodsController.class);

	@Autowired
	private DragGoodsService drGoodsService;
	
	/**
	 * 查询所有的恐龙骨兑换商品
	 * @return
	 */
	@RequestMapping(value = "/list", method = {RequestMethod.POST,RequestMethod.GET})
	public @ResponseBody ResponseEntity<List<DragGoodsVo>> listGoods() {
		List<DragGoodsVo> rows= drGoodsService.listGoods();
		return new ResponseEntity<List<DragGoodsVo>>(rows, HttpStatus.OK);
	}
	
	/**
	 * 查询恐龙骨兑换商品详情
	 * @param goodsId
	 * @return
	 */
	@RequestMapping(value = "/detail", method = {RequestMethod.POST,RequestMethod.GET})
	public @ResponseBody ResponseEntity<UserTicketTemplateVo> detail(@RequestParam(required = true) int goodsId) {
		UserTicketTemplateVo detailVo = drGoodsService.goodsDetail(goodsId);
		return new ResponseEntity<UserTicketTemplateVo>(detailVo, HttpStatus.OK);
	}
	
	/**
	 * 恐龙骨立即兑换
	 * @param form
	 * @return
	 */
	@RequestMapping(value = "/exchange", method = {RequestMethod.POST,RequestMethod.GET})
	public @ResponseBody ResponseEntity<BaseResponse> exchange(@RequestBody DragBoneForm form) {
		BaseResponse br = drGoodsService.exchange(form);
		return new ResponseEntity<BaseResponse>(br, HttpStatus.OK);
	}
	
	/**
	 * 查询恐龙骨兑换记录
	 * @param openid
	 * @return
	 */
	@RequestMapping(value = "/listrecord", method = {RequestMethod.POST,RequestMethod.GET})
	public @ResponseBody ResponseEntity<List<UserDragUsedRecordVo>> listRecord(@RequestParam(required = true) String openid) {
		List<UserDragUsedRecordVo> rows= drGoodsService.listRecord(openid);
		return new ResponseEntity<List<UserDragUsedRecordVo>>(rows, HttpStatus.OK);
	}
	
	/**
	 * 查询恐龙骨详情
	 * @param openid
	 * @return
	 */
	@RequestMapping(value = "/listallrecord", method = {RequestMethod.POST,RequestMethod.GET})
	public @ResponseBody ResponseEntity<List<UserDragUsedRecordVo>> listAllRecord(@RequestParam(required = true) String openid) {
		List<UserDragUsedRecordVo> rows= drGoodsService.listAllRecord(openid);
		return new ResponseEntity<List<UserDragUsedRecordVo>>(rows, HttpStatus.OK);
	}
	
}
