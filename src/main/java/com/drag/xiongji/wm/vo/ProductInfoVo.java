package com.drag.xiongji.wm.vo;

import java.math.BigDecimal;

import javax.persistence.Id;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商品
 * @author longyunbo
 *
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class ProductInfoVo {
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
	private String createTime;
	/**
	 * 修改时间
	 */
	private String updateTime;
	/**
	 * 是否结束，1，是；0，否
	 */
	private int isEnd;
	/**
	 * 是否销售
	 */
	private int isSale;
	/**
	 * 购买人数
	 */
	private int succTimes;

}
