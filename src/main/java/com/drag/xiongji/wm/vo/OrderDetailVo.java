package com.drag.xiongji.wm.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商品信息详细表
 * @author longyunbo
 *
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class OrderDetailVo implements Serializable {

	private static final long serialVersionUID = 4622512998684573114L;
	private int id;
	/**
	 * 订单编号
	 */
	private String orderid;
	/**
	 * 商品类型,yj-有机食品,ly-旅游服务,qy-企业服务,tc-营养套餐
	 */
	private String type;
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
	 * 用户编号
	 */
	private int uid;
	/**
	 * 购买规格
	 */
	private String norms;
	/**
	 * 商品数量
	 */
	private int number;
	/**
	 * 消耗积分
	 */
	private BigDecimal price;
	/**
	 * 创建时间
	 */
	private String createTime;
	/**
	 * 修改时间
	 */
	private String updateTime;
	
	

}
