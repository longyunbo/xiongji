package com.drag.xiongji.wm.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.drag.xiongji.wm.service.ProductService;
import com.drag.xiongji.wm.vo.ProductActivityVo;
import com.drag.xiongji.wm.vo.ProductInfoVo;


@RestController
@RequestMapping(value = "/xiongji/product", produces = "application/json;charset=utf-8")
public class ProductController {
	
	private final static Logger log = LoggerFactory.getLogger(ProductController.class);

	@Autowired
	private ProductService productInfoService;
	
	/**
	 * 根据类型查询查询商品
	 * @return
	 */
	@RequestMapping(value = "/listgoods", method = {RequestMethod.POST,RequestMethod.GET})
	public @ResponseBody ResponseEntity<List<ProductInfoVo>> listGoods(@RequestParam String type,@RequestParam String orderby) {
		List<ProductInfoVo> rows= productInfoService.listGoods(type,orderby);
		return new ResponseEntity<List<ProductInfoVo>>(rows, HttpStatus.OK);
	}
	
	/**
	 * 商品搜索
	 * @param type
	 * @param orderby
	 * @return
	 */
	@RequestMapping(value = "/listsearch", method = {RequestMethod.POST,RequestMethod.GET})
	public @ResponseBody ResponseEntity<List<ProductInfoVo>> listSearch(@RequestParam String name) {
		List<ProductInfoVo> rows= productInfoService.listGoodsByName(name);
		return new ResponseEntity<List<ProductInfoVo>>(rows, HttpStatus.OK);
	}
	
	/**
	 * 热门搜索
	 * @return
	 */
	@RequestMapping(value = "/hotsearch", method = {RequestMethod.POST,RequestMethod.GET})
	public @ResponseBody ResponseEntity<List<String>> hotSearch() {
		List<String> rows = new ArrayList<String>();
		rows.add("越南香芒鸡扒沙拉");
		rows.add("吞拿鱼三明治");
		return new ResponseEntity<List<String>>(rows, HttpStatus.OK);
	}
	
	/**
	 * 配送费
	 * 餐盒费
	 * @return
	 */
	@RequestMapping(value = "/deliver", method = {RequestMethod.POST,RequestMethod.GET})
	public @ResponseBody ResponseEntity<JSONObject> deliver() {
		JSONObject json = new JSONObject();
		json.put("chf", 0);
		json.put("psf", 0);
		return new ResponseEntity<JSONObject>(json, HttpStatus.OK);
	}
	
	/**
	 * 商品详情
	 * @param goodsId
	 * @return
	 */
	@RequestMapping(value = "/detail", method = {RequestMethod.POST,RequestMethod.GET})
	public @ResponseBody ResponseEntity<ProductInfoVo> detail(@RequestParam(required = true) int goodsId) {
		ProductInfoVo detailVo = productInfoService.goodsDetail(goodsId);
		return new ResponseEntity<ProductInfoVo>(detailVo, HttpStatus.OK);
	}
	
	/**
	 * 查询所有商品活动、
	 * @return
	 */
	@RequestMapping(value = "/listactivity", method = {RequestMethod.POST,RequestMethod.GET})
	public @ResponseBody ResponseEntity<List<ProductActivityVo>> listActivity() {
		List<ProductActivityVo> rows= productInfoService.listActivity();
		return new ResponseEntity<List<ProductActivityVo>>(rows, HttpStatus.OK);
	}
	
	
}
