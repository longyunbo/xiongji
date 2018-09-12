package com.drag.xiongji.pt.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "pt_order")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class PtOrder implements Serializable {
	
	// 订单状态0-已付款，1-已退款，2-退款失败
	public static final int ORDERSTATUS_SUCCESS = 0;
	public static final int ORDERSTATUS_RETURN = 1;
	public static final int ORDERSTATUS_FAIL = 2;
	//是否团长 0-不是 1-是
	public static final int ISHEADER_YES = 1;
	public static final int ISHEADER_NO = 0;

	private static final long serialVersionUID = 3427636144202439207L;
	/**
	 * 自增id
	 */
	@Id
	private int id;
	/**
	 * 拼团商品id
	 */
	private int ptgoodsId;
	/**
	 * 拼团编号
	 */
	private String ptcode;
	/**
	 * 用户id
	 */
	private int uid;
	/**
	 * 退款单编号
	 */
	private String ptrefundcode;
	/**
	 * 商品名称
	 */
	private String ptgoodsName;
	/**
	 * 单价
	 */
	private BigDecimal perPrice;
	/**
	 * 付款金额
	 */
	private BigDecimal price;
	/**
	 * 参团日期
	 */
	private Date addtime;
	/**
	 * 手机号码
	 */
	private String tel;
	/**
	 * 0:初始，1:已付款, 2-已发货 3-已收货 4-已评价,5:已退款
	 */
	private int orderstatus;
	/**
	 * 是否团长 0-不是 1-是
	 */
	private int isHeader;
	/**
	 * 数量
	 */
	private int number;
	/**
	 * 创建日期
	 */
	private Date createTime;
	/**
	 * 商户订单号
	 */
	private String outTradeNo;

}
