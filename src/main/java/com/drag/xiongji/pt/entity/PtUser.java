package com.drag.xiongji.pt.entity;

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
@Table(name = "pt_user")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class PtUser implements Serializable {
	
	// 是否团长 0-不是 1-是
	public static final int ISHEADER_YES = 1;
	public static final int ISHEADER_NO = 0;
	//0:未付款 1:拼团中，2:拼团成功, 3：拼团失败
	public static final int PTSTATUS_UNPAID = 0;
	public static final int PTSTATUS_MIDDLE = 1;
	public static final int PTSTATUS_SUCC= 2;
	public static final int PTSTATUS_FAIL = 3;
	
	private static final long serialVersionUID = -6266882106264125109L;
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
	 * 拼团商品id
	 */
	private int ptgoodsId;
	/**
	 * 拼团编号
	 */
	private String ptcode;
	/**
	 * 是否团长 0-不是 1-是
	 */
	private int isHeader;
	/**
	 * 购买数量
	 */
	private int number;
	
	/**
	 * 拼团人数
	 */
	private int ptSize;
	/**
	 * 创建日期
	 */
	private Date createTime;
	/**
	 * 0:未付款 1:拼团中，2:拼团成功, 3：拼团失败
	 */
	private int ptstatus;
	/**
	 * formId
	 */
	private String formId;
	/**
	 * 消息发送状态:0-未发送,1-已发送,2-发送失败
	 */
	private int sendstatus;
}
