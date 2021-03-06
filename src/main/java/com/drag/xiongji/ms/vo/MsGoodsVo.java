package com.drag.xiongji.ms.vo;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper=false)
public class MsGoodsVo{
	
	private int msgoodsId;
	/**
	 * 秒杀商品名称
	 */
	private String msgoodsName;
	/**
	 * 秒杀价格
	 */
	private BigDecimal price;
	/**
	 * 商品价格(默认价格)
	 */
	private BigDecimal msPrice;
	/**
	 * 秒杀人数(2-10)
	 */
//	private int msSize;
	/**
	 * 秒杀有效期(默认24小时)
	 */
	private int msValidhours;
	/**
	 * 秒杀开始时间
	 */
	private String startTime;
	/**
	 * 秒杀结束时间
	 */
	private String endTime;
	/**
	 * 商品库存数量
	 */
	private int msgoodsNumber;
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
	private String msgoodsThumb;
	/**
	 * 商品详情轮播图
	 */
	private String msgoodsImgs;
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
	 * 秒杀人数
	 */
	private int msTimes;
	/**
	 * 完成秒杀人数
	 */
	private int msSuccTimes;
}

