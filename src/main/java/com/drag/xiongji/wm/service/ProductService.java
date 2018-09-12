package com.drag.xiongji.wm.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drag.xiongji.utils.BeanUtils;
import com.drag.xiongji.utils.DateUtil;
import com.drag.xiongji.utils.StringUtil;
import com.drag.xiongji.wm.dao.ProductActivityDao;
import com.drag.xiongji.wm.dao.ProductInfoDao;
import com.drag.xiongji.wm.entity.ProductActivity;
import com.drag.xiongji.wm.entity.ProductInfo;
import com.drag.xiongji.wm.vo.ProductActivityVo;
import com.drag.xiongji.wm.vo.ProductInfoVo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProductService {

	@Autowired
	private ProductInfoDao productDao;
	@Autowired
	private ProductActivityDao productActivityDao;

	/**
	 * 查询商品列表
	 * @return
	 */
	public List<ProductInfoVo> listGoods(String type,String orderby) {
		log.info("【外卖类商品列表查询传入参数】type = {},orderby = {}",type,orderby);
		List<ProductInfoVo> goodsResp = new ArrayList<ProductInfoVo>();
		List<ProductInfo> goodsList = new ArrayList<ProductInfo>();
		if(!StringUtil.isEmpty(orderby)) {
			if("desc".equals(orderby)) {
				goodsList = productDao.findByTypeOrderByPriceDesc(type);
			}else {
				goodsList = productDao.findByTypeOrderByPriceAsc(type);
			}
		}else {
			goodsList = productDao.findByTypeOrderBySuccTimesDesc(type);
		}
		if (goodsList != null && goodsList.size() > 0) {
			for (ProductInfo goods : goodsList) {
				ProductInfoVo resp = new ProductInfoVo();
				this.copyProperties(goods, resp);
				goodsResp.add(resp);
			}
		}
		return goodsResp;
	}
	
	/**
	 * 商品搜索
	 * @param name
	 * @return
	 */
	public List<ProductInfoVo> listGoodsByName(String name) {
		log.info("【外卖类商品列表查询传入参数】name = {}",name);
		List<ProductInfoVo> goodsResp = new ArrayList<ProductInfoVo>();
		List<ProductInfo> goodsList = new ArrayList<ProductInfo>();
		if(!StringUtil.isEmpty(name)) {
			goodsList = productDao.findByNameLike(name);
		}else {
			goodsList = productDao.findByTypeOrderBySuccTimesDesc("rx");
		}
		if (goodsList != null && goodsList.size() > 0) {
			for (ProductInfo goods : goodsList) {
				ProductInfoVo resp = new ProductInfoVo();
				this.copyProperties(goods, resp);
				goodsResp.add(resp);
			}
		}
		return goodsResp;
	}
	
	/**
	 * 查询所有活动
	 * @return
	 */
	public List<ProductActivityVo> listActivity() {
		List<ProductActivityVo> goodsResp = new ArrayList<ProductActivityVo>();
		List<ProductActivity> goodsList = new ArrayList<ProductActivity>();
		goodsList = productActivityDao.findAll();
		if (goodsList != null && goodsList.size() > 0) {
			for (ProductActivity goods : goodsList) {
				ProductActivityVo resp = new ProductActivityVo();
				BeanUtils.copyProperties(goods, resp,new String[]{"createTime", "updateTime"});
				resp.setCreateTime((DateUtil.format(goods.getCreateTime(), "yyyy-MM-dd HH:mm:ss")));
				resp.setUpdateTime((DateUtil.format(goods.getUpdateTime(), "yyyy-MM-dd HH:mm:ss")));
				goodsResp.add(resp);
			}
		}
		return goodsResp;
	}
	
	
	/**
	 * 查询商品详情
	 * @return
	 */
	public ProductInfoVo goodsDetail(int goodsId) {
		log.info("【外卖类商品详情查询传入参数】goodsId = {}",goodsId);
		ProductInfoVo detailVo = new ProductInfoVo();
		ProductInfo goods = productDao.findGoodsDetail(goodsId);
		if(goods != null) {
			this.copyProperties(goods, detailVo);
		}
		return detailVo;
	}
	
	/**
	 * 提取copy方法
	 * @param goods
	 * @param detailVo
	 */
	public void copyProperties(ProductInfo goods,ProductInfoVo detailVo) {
		BeanUtils.copyProperties(goods, detailVo,new String[]{"createTime", "updateTime"});
		detailVo.setCreateTime((DateUtil.format(goods.getCreateTime(), "yyyy-MM-dd HH:mm:ss")));
		detailVo.setUpdateTime((DateUtil.format(goods.getUpdateTime(), "yyyy-MM-dd HH:mm:ss")));
	}
	
}
