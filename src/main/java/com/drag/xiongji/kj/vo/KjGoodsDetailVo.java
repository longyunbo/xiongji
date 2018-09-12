package com.drag.xiongji.kj.vo;

import java.math.BigDecimal;
import java.util.List;

import com.drag.xiongji.user.vo.UserVo;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper=false)
public class KjGoodsDetailVo{
	
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
	 * 砍价人数(2-10)
	 */
	private int kjSize;
	/**
	 * 砍价有效期(默认24小时)
	 */
	private int kjValidhours;
	/**
	 * 砍价开始时间
	 */
	private String startTime;
	/**
	 * 砍价结束时间
	 */
	private String endTime;
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
	/**
	 * 团长
	 */
	List<UserVo> groupers;
}

