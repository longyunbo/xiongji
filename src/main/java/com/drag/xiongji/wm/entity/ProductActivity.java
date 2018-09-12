package com.drag.xiongji.wm.entity;

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

/**
 * 商品活动
 * @author longyunbo
 *
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "t_product_activity")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class ProductActivity implements Serializable {
	
	private static final long serialVersionUID = -7038140663701932288L;
	/**
	 * 自增id
	 */
	@Id
	private int id;
	/**
	 * 活动名称
	 */
	private String activityName;
	/**
	 * 优惠券类型：1-满减（订单直接减去金额），2-限时促销活动
	 */
	private int type;
	/**
	 * 0-未启用，1-已过期，2-已启用
	 */
	private int status;
	/**
	 * 内容
	 */
	private String content;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 修改时间
	 */
	private Date updateTime;

}
