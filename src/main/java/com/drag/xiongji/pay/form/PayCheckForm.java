package com.drag.xiongji.pay.form;

import lombok.Data;

@Data
public class PayCheckForm {
	/**
	 * 商品id
	 */
	private int goodsId;
	/**
	 * 用户id
	 */
	private String openid;
	/**
	 * 数量
	 */
	private int number;
	/**
	 * 类型
	 */
	private String type;
	
	//拼团code
	private String code;
	
}
