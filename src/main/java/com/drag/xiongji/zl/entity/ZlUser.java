package com.drag.xiongji.zl.entity;

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
@Table(name = "zl_user")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class ZlUser implements Serializable {
	
	private static final long serialVersionUID = -7020985360414478174L;
	// 是否团长 0-不是 1-是
	public static final int ISHEADER_YES = 1;
	public static final int ISHEADER_NO = 0;
	//0:未付款 1:助力中，2:助力成功, 3：助力失败
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
	 * 助力商品id
	 */
	private int zlgoodsId;
	/**
	 * 助力商品名称
	 */
	private String zlgoodsName;
	/**
	 * 助力编号
	 */
	private String zlcode;
	/**
	 * 默认金额
	 */
	private BigDecimal zlPrice;
	/**
	 * 助力价格
	 */
//	private BigDecimal price;
	/**
	 * 是否团长 0-不是 1-是
	 */
	private int isHeader;
	/**
	 * 助力人数
	 */
	private int zlSize;
	/**
	 * 创建日期
	 */
	private Date createTime;
	/**
	 * 0:未付款 1:助力中，2:助力成功, 3：助力失败
	 */
	private int zlstatus;
	/**
	 * formId
	 */
	private String formId;
	/**
	 * 消息发送状态:0-未发送,1-已发送,2-发送失败
	 */
	private int sendstatus;

}
