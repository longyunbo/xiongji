package com.drag.xiongji.zl.entity;

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
@Table(name = "zl_goods")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class ZlGoods implements Serializable {

	private static final long serialVersionUID = 4149641496579747798L;
	/**
	 * 助力商品自增id
	 */
	@Id
	private int zlgoodsId;
	/**
	 * 助力商品名称
	 */
	private String zlgoodsName;
	/**
	 * 商品默认价格
	 */
	private BigDecimal zlPrice;
	/**
	 * 助力人数规模(2-10)
	 */
	private int zlSize;
	/**
	 * 有效期(默认24小时)
	 */
	private int zlValidhours;
	/**
	 * 助力开始时间
	 */
	private Date startTime;
	/**
	 * 助力结束时间
	 */
	private Date endTime;
	/**
	 * 商品库存数量
	 */
	private int zlgoodsNumber;
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
	private String zlgoodsThumb;
	/**
	 * 商品详情轮播图
	 */
	private String zlgoodsImgs;
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
	 * 助力人数
	 */
	private int zlTimes;
	/**
	 * 完成助力人数
	 */
	private int zlSuccTimes;
	/**
	 * 活动权限
	 */
	private String auth;
}
