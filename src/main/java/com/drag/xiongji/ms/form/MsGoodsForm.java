package com.drag.xiongji.ms.form;

import lombok.Data;

@Data
public class MsGoodsForm {
	
	/**
	 * 秒杀商品id
	 */
	private int msgoodsId;
	/**
	 * 用户id
	 */
	private String openid;
	/**
	 * 商品名称
	 */
	private String msgoodsName;
	/**
	 * 提醒类型 '0-开抢提醒,1-取消提醒',
	 */
	private int remindType;
	/**
	 * 数量
	 */
	private int number;
	/**
	 * formId
	 */
	private String formId;
	/**
	 * 商户订单号
	 */
	private String outTradeNo;
	
	
}
