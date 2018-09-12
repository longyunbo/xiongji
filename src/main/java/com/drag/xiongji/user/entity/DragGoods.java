package com.drag.xiongji.user.entity;

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
@Table(name = "drag_goods")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class DragGoods implements Serializable {

	private static final long serialVersionUID = -7019193751237613050L;
	/**
	 * 恐龙骨商品自增id
	 */
	@Id
	private int drgoodsId;
	/**
	 * 恐龙骨商品名称
	 */
	private String drgoodsName;
	/**
	 * 恐龙骨价格
	 */
	private BigDecimal price;
	/**
	 * 商品价格(默认价格)
	 */
	private BigDecimal drPrice;
	/**
	 * 恐龙骨开始时间
	 */
	private Date startTime;
	/**
	 * 恐龙骨结束时间
	 */
	private Date endTime;
	/**
	 * 商品库存数量
	 */
	private int drgoodsNumber;
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
	private String drgoodsThumb;
	/**
	 * 商品详情轮播图
	 */
	private String drgoodsImgs;
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
	 * 兑换人数
	 */
	private int drSuccTimes;

}
