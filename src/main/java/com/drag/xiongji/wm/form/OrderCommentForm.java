package com.drag.xiongji.wm.form;

import lombok.Data;

/**
 * 订单form
 * @author longyunbo
 *
 */
@Data
public class OrderCommentForm {
	
	/**
	 * 订单号
	 */
	private String orderid;
	/**
	 * 用户id
	 */
	private String openid;
	/**
	 * 商品编号
	 */
	private int goodsId;
	/**
	 * 商品名称
	 */
	private String goodsName;
	/**
	 * 好评状态,0:好评,1:中评,2:差评
	 */
	private int commentstatus;
	/**
	 * 好评星数
	 */
	private int commentlevel;
	/**
	 * 商品评论
	 */
	private String comment;
	/**
	 * 评论图片
	 */
	private String commentimg;
	
}
