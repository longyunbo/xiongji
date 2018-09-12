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
 * 有机食材
 * @author longyunbo
 *
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "t_product_info")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class ProductInfo implements Serializable {

	private static final long serialVersionUID = 5881992527238032246L;
	
	/**
	 * 商品自增id
	 */
	@Id
	private int goodsId;
	/**
	 * 商品名称
	 */
	private String goodsName;
	/**
	 * 商品类型,rx-热销,zk-折扣,xy-鲜鸭,ms-美素
	 */
	private String type;
	/**
	 * 购买规格
	 */
	private String norms;
	/**
	 * 默认价格
	 */
	private BigDecimal defPrice;
	/**
	 * 消耗金额
	 */
	private BigDecimal price;
	/**
	 * 商品剪短描述
	 */
	private String description;
	/**
	 * 商品详细描述
	 */
	private String content;
	/**
	 * 经验值
	 */
//	private int exp;
	/**
	 * 商品微缩图
	 */
	private String goodsThumb;
	/**
	 * 商品详情轮播图
	 */
	private String goodsImgs;
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
	 * 购买人数
	 */
	private int succTimes;

}
