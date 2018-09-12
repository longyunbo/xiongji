package com.drag.xiongji.dwd.form;

import lombok.Data;

@Data
public class CallBackForm {
	/**
	 * 渠道订单编号
	 */
	private String order_original_id;
	/**
	 * 订单状态
	 */
	private int order_status;
	/**
	 * 更新时间戳
	 */
	private Long time_status_update;
	/**
	 * 验证签名
	 */
	private String sig;
	/**
	 * 订单取消原因
	 */
	private String cancel_reason;
	
}
