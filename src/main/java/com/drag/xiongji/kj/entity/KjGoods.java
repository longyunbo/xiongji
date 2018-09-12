package com.drag.xiongji.kj.entity;

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
@Table(name = "kj_goods")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class KjGoods implements Serializable {

	private static final long serialVersionUID = -3555762560209401851L;
	/**
	 * 砍价商品自增id
	 */
	@Id
	private int kjgoodsId;
	/**
	 * 砍价商品名称
	 */
	private String kjgoodsName;
	/**
	 * 砍价价格
	 */
	private BigDecimal price;
	/**
	 * 商品价格(默认价格)
	 */
	private BigDecimal kjPrice;
	/**
	 * 砍价人数规模(2-10)
	 */
	private int kjSize;
	/**
	 * 有效期(默认24小时)
	 */
	private int kjValidhours;
	/**
	 * 砍价开始时间
	 */
	private Date startTime;
	/**
	 * 砍价结束时间
	 */
	private Date endTime;
	/**
	 * 商品库存数量
	 */
	private int kjgoodsNumber;
	/**
	 * 商品剪短描述
	 */
	private String description;
	/**
	 * 商品详细描述
	 */
	private String content;
	/**
	 * 恐龙骨
	 */
	private int dragBone;
	/**
	 * 经验值
	 */
	private int exp;
	/**
	 * 商品微缩图
	 */
	private String kjgoodsThumb;
	/**
	 * 商品详情轮播图
	 */
	private String kjgoodsImgs;
	/**
	 * 该商品显示顺序（越大越靠后）
	 */
	private int sort;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 修改时间
	 */
	private Date updateTime;
	/**
	 * 是否结束，1，是；0，否
	 */
	private int isEnd;
	/**
	 * 砍价人数
	 */
	private int kjTimes;
	/**
	 * 完成砍价人数
	 */
	private int kjSuccTimes;
	/**
	 * 活动权限
	 */
	private String auth;
}
