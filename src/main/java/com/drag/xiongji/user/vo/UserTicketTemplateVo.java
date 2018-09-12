package com.drag.xiongji.user.vo;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Id;

import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 会员卡券模板表
 * @author longyunbo
 *
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class UserTicketTemplateVo{

	/**
	 * 自增id
	 */
	@Id
	private int id;
	/**
	 * 商品编号
	 */
	private int goodsId;
	/**
	 * 类型:pt-拼团,ms-秒杀,zl-助力,kj-砍价,drag-恐龙骨
	 */
	private String type;
	/**
	 * 卡券名称
	 */
	private String ticketName;
	/**
	 * 卡券二维码
	 */
	private String ticketCode;
	/**
	 * 商品剪短描述
	 */
	private String description;
	/**
	 * 商品优惠说明
	 */
	private String preferential;
	/**
	 * 商品微缩图
	 */
	private String goodsThumb;
	/**
	 * 商品价格(默认价格)
	 */
	private BigDecimal defprice;
	/**
	 * 付款价格
	 */
	private BigDecimal price;
	/**
	 * 恐龙骨
	 */
	private int dragBone;
	/**
	 * 使用期限
	 */
	private int term;
	/**
	 * 可用时段：0-周一至周五，1-周六至周日
	 */
	private int availableTime;
	/**
	 * 可用门店
	 */
	private String availableMerchant;
	/**
	 * 商品详细描述
	 */
	private String content;
	/**
	 * 创建时间
	 */
	private String createTime;

}
