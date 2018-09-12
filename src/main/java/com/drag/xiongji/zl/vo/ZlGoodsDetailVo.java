package com.drag.xiongji.zl.vo;

import java.math.BigDecimal;
import java.util.List;

import com.drag.xiongji.user.vo.UserVo;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper=false)
public class ZlGoodsDetailVo{
	
	private int zlgoodsId;
	/**
	 * 助力商品名称
	 */
	private String zlgoodsName;
	/**
	 * 商品价格(默认价格)
	 */
//	private BigDecimal price;
	/**
	 * 商品价格(默认价格)
	 */
	private BigDecimal zlPrice;
	/**
	 * 助力人数(2-10)
	 */
	private int zlSize;
	/**
	 * 助力有效期(默认24小时)
	 */
	private int zlValidhours;
	/**
	 * 助力开始时间
	 */
	private String startTime;
	/**
	 * 助力结束时间
	 */
	private String endTime;
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
	/**
	 * 团长开始时间
	 */
	private String groupStartTime;
	
	/**
	 * 团长
	 */
	List<UserVo> groupers;
}

