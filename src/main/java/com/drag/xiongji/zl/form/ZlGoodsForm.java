package com.drag.xiongji.zl.form;

import lombok.Data;

@Data
public class ZlGoodsForm {
	
	/**
	 * 助力商品id
	 */
	private int zlgoodsId;
	/**
	 * 用户id
	 */
	private String openid;
	/**
	 * 退款单编号
	 */
//	private String zlrefundcode;
	/**
	 * 商品名称
	 */
	private String zlgoodsName;
	/**
	 * 单价
	 */
//	private BigDecimal perPrice;
	/**
	 * 助力金额
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
	 * 助力人数(规模)
	 */
//	private int zlSize;
	/**
	 * 助力编号
	 */
	private String zlCode;
	/**
	 * formId
	 */
	private String formId;
	
}
