package com.drag.xiongji.wm.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商品信息
 * @author longyunbo
 *
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class OrderInfoVo implements Serializable {
	
	private static final long serialVersionUID = 4674677983620486166L;
	private int id;
	/**
	 * 订单编号
	 */
	private String orderid;
	/**
	 * 商品类型,rx-热销,zk-折扣,xy-鲜鸭,ms-美素
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
	private String goodsImg;
	/**
	 * 用户编号
	 */
	private int uid;
	/**
	 * 买家姓名
	 */
	private String buyName;
	/**
	 * 买家电话
	 */
	private String phone;
	/**
	 * 商品总数量
	 */
	private int number;
	/**
	 * 消耗金额
	 */
	private BigDecimal price;
	/**
	 * 优惠金额
	 */
	private BigDecimal dicprice;
	/**
	 * 订单总金额
	 */
	private BigDecimal tolprice;
	/**
	 * 商户订单号
	 */
	private String outTradeNo;
	/**
	 * 购买规格
	 */
	private String norms;
	/**
	 * 订单状态，0:未付款,1:已付款, 2:已退款,3:已评价'
	 */
	private int orderstatus;
	/**
	 * 支付方式,wx-微信,lpk-礼品卡菜品券,cash-礼品卡现金券
	 */
	private String payType;
	/**
	 * 送餐状态，0：派单中，3：已转单，5：取餐中，10：已到店，15：配送中，98：异常，99：已取消，100：已到达
	 */
	private int deliverystatus;
	/**
	 * 收货人姓名
	 */
	private String receiptName;
	/**
	 * 收货人电话
	 */
	private String receiptTel;
	/**
	 * 收货区域信息
	 */
	private String region;
	/**
	 * 邮政编码
	 */
	private String postalcode;
	/**
	 * 收货信息地址
	 */
	private String receiptAddress;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 确认收货时间
	 */
	private String confirmReceiptTime;
	/**
	 * 退款单号
	 */
	private String refundcode;
//	/**
//	 * 好评状态,0:好评,1:中评,2:差评
//	 */
//	private int commentstatus;
//	/**
//	 * 好评星数
//	 */
//	private int commentlevel;
//	/**
//	 * 商品评论
//	 */
//	private String comment;
	/**
	 * 创建时间
	 */
	private String createTime;
	/**
	 * 修改时间
	 */
	private String updateTime;

}
