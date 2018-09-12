package com.drag.xiongji.kj.form;

import lombok.Data;

@Data
public class KjGoodsForm {
	
	/**
	 * 砍价商品id
	 */
	private int kjgoodsId;
	/**
	 * 用户id
	 */
	private String openid;
	/**
	 * 退款单编号
	 */
//	private String kjrefundcode;
	/**
	 * 商品名称
	 */
	private String kjgoodsName;
	/**
	 * 单价
	 */
//	private BigDecimal perPrice;
	/**
	 * 砍价金额
	 */
//	private BigDecimal price;
	/**
	 * 手机
	 */
	private String tel;
	/**
	 * 数量
	 */
//	private int number;
	/**
	 * 砍价人数(规模)
	 */
//	private int kjSize;
	/**
	 * 砍价编号
	 */
	private String kjCode;
	/**
	 * formId
	 */
	private String formId;
}
