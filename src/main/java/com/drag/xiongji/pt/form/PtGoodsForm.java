package com.drag.xiongji.pt.form;

import lombok.Data;

@Data
public class PtGoodsForm {
	
	/**
	 * 拼团商品id
	 */
	private int ptgoodsId;
	/**
	 * 用户id
	 */
	private String openid;
	/**
	 * 退款单编号
	 */
//	private String ptrefundcode;
	/**
	 * 商品名称
	 */
//	private String ptgoodsName;
	/**
	 * 单价
	 */
//	private BigDecimal perPrice;
	/**
	 * 付款金额
	 */
//	private BigDecimal price;
	/**
	 * 手机
	 */
//	private String tel;
	/**
	 * 数量
	 */
	private int number;
	/**
	 * 拼团人数(规模)
	 */
//	private int ptSize;
	/**
	 * 拼团编号
	 */
	private String ptCode;
	/**
	 * formId
	 */
	private String formId;
	/**
	 * 微信商户订单号
	 */
	private String outTradeNo;
	
}
