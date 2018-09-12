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
@Table(name = "pt_goods")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class PtGoods implements Serializable {

	private static final long serialVersionUID = -6288140580725198724L;
	/**
	 * 拼团商品自增id
	 */
	@Id
	private int ptgoodsId;
	/**
	 * 拼团商品名称
	 */
	private String ptgoodsName;
	/**
	 * 商品价格
	 */
	private BigDecimal price;
	/**
	 * 商品价格(默认价格)
	 */
	private BigDecimal ptPrice;
	/**
	 * 拼团人数规模(2-10)
	 */
	private int ptSize;
	/**
	 * 拼团有效期(默认24小时)
	 */
	private int ptValidhours;
	/**
	 * 拼团开始时间
	 */
	private Date startTime;
	/**
	 * 拼团结束时间
	 */
	private Date endTime;
	/**
	 * 商品库存数量
	 */
	private int ptgoodsNumber;
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
	private String ptgoodsThumb;
	/**
	 * 商品详情轮播图
	 */
	private String ptgoodsImgs;
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
	 * 拼团人数
	 */
	private int ptTimes;
	/**
	 * 完成拼团人数
	 */
	private int ptSuccTimes;
	/**
	 * 活动权限
	 */
	private String auth;
	

}
