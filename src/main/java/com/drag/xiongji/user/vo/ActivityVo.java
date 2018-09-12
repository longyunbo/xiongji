package com.drag.xiongji.user.vo;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper=false)
public class ActivityVo{
	
	private int goodsId;
	/**
	 * 商品名称
	 */
	private String goodsName;
	/**
	 * 类型:pt-拼团,ms-秒杀,zl-助力,kj-砍价
	 */
	private String type;
	/**
	 * 价格
	 */
	private BigDecimal price;
	/**
	 * 商品价格(默认价格)
	 */
	private BigDecimal defPrice;
	/**
	 * 人数(2-10)
	 */
	private int Size;
	/**
	 * 有效期(默认24小时)
	 */
	private int Validhours;
	/**
	 * 开始时间
	 */
	private String startTime;
	/**
	 * 砍价结束时间
	 */
	private String endTime;
	/**
	 * 商品库存数量
	 */
	private int goodsNumber;
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
	 * 人数
	 */
	private int times;
	/**
	 * 完成人数
	 */
	private int succTimes;
	/**
	 * 状态
	 */
	private int status;
	
	private String code;
	
	private String uid;
	
	private String grouperId;
	
	private String grouperCreateTime;
}

