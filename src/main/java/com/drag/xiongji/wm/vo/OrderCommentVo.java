package com.drag.xiongji.wm.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商品信息表
 * @author longyunbo
 *
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class OrderCommentVo {
	
	private int id;
	/**
	 * 订单编号
	 */
	private String orderid;
	/**
	 * 用户编号
	 */
	private int uid;
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
	 * 商品评价图片
	 */
	private String commentimg;
	/**
	 * 创建时间
	 */
	private String createTime;

}
