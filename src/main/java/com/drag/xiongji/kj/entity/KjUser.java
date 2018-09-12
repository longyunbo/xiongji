package com.drag.xiongji.kj.entity;

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
@Table(name = "kj_user")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class KjUser implements Serializable {
	
	private static final long serialVersionUID = 2233617972011354703L;
	// 是否团长 0-不是 1-是
	public static final int ISHEADER_YES = 1;
	public static final int ISHEADER_NO = 0;
	//0:未付款 1:砍价中，2:砍价成功, 3：砍价失败
	public static final int PTSTATUS_UNPAID = 0;
	public static final int PTSTATUS_MIDDLE = 1;
	public static final int PTSTATUS_SUCC= 2;
	public static final int PTSTATUS_FAIL = 3;
	
	/**
	 * 自增id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	/**
	 * 用户id
	 */
	private int uid;
	/**
	 * 团长id
	 */
	private int grouperId;
	/**
	 * 砍价商品id
	 */
	private int kjgoodsId;
	/**
	 * 砍价商品名称
	 */
	private String kjgoodsName;
	/**
	 * 砍价编号
	 */
	private String kjcode;
	/**
	 * 需砍掉金额
	 */
	private BigDecimal kjPrice;
	/**
	 * 砍价价格
	 */
	private BigDecimal price;
	/**
	 * 是否团长 0-不是 1-是
	 */
	private int isHeader;
	/**
	 * 砍价人数
	 */
	private int kjSize;
	/**
	 * 创建日期
	 */
	private Date createTime;
	/**
	 * 0:未付款 1:砍价中，2:砍价成功, 3：砍价失败
	 */
	private int kjstatus;
	/**
	 * formId
	 */
	private String formId;
	
	/**
	 * 消息发送状态:0-未发送,1-已发送,2-发送失败
	 */
	private int sendstatus;
}
