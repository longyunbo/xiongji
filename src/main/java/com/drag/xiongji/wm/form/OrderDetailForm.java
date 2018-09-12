package com.drag.xiongji.wm.form;

import java.math.BigDecimal;

import lombok.Data;

/**
 * 订单详情form
 * @author longyunbo
 *
 */
@Data
public class OrderDetailForm {
	/**
	 * 商品编号
	 */
	private int goodsId;
	/**
	 * 商品名称
	 */
	private String goodsName;
	/**
	 * 商品图片
	 */
	private String goodsThumb;
	/**
	 * 类型
	 */
	private String type;
	/**
	 * 购买规格
	 */
	private String norms;
	/**
	 * 商品数量
	 */
	private int number;
	/**
	 * 消耗金额
	 */
	private BigDecimal price;
	

}
