package com.drag.xiongji.ms.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(name = "ms_order")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class MsOrder implements Serializable {
	
	private static final long serialVersionUID = 7363586044946749105L;
	// 订单状态0-已付款，1-已退款
	public static final int ORDERSTATUS_SUCCESS = 0;
	public static final int ORDERSTATUS_RETURN = 1;

	/**
	 * 自增id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	/**
	 * 秒杀商品id
	 */
	private int msgoodsId;
	/**
	 * 用户id
	 */
	private int uid;
	/**
	 * 退款单编号
	 */
	private String msrefundcode;
	/**
	 * 商品名称
	 */
	private String msgoodsName;
	/**
	 * 单价
	 */
	private BigDecimal perPrice;
	/**
	 * 付款金额
	 */
	private BigDecimal price;
	/**
	 * 手机号码
	 */
//	private String tel;
	/**
	 *订单状态0-已付款，1-已退款
	 */
	private int orderstatus;
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
	/**
	 * formId
	 */
	private String formId;
	

}
