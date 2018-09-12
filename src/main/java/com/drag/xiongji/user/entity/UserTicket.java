package com.drag.xiongji.user.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
 * 会员卡券表
 * @author longyunbo
 *
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "t_user_ticket")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class UserTicket implements Serializable {

	private static final long serialVersionUID = 9040973201457873437L;
	
	
	// 状态:0-未使用 1-已使用 2-已过期 3-赠送中
	public static final int STATUS_NO = 0;
	public static final int STATUS_YES = 1;
	public static final int STATUS_OVER = 2;
	public static final int STATUS_SEND = 3;
	public static final int STATUS_NORMAL = 4;
	
	/**
	 * 自增id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	private int id;
	/**
	 * 用户id
	 */
	private int uid;
	/**
	 * 赠送者编号(礼品卡使用)
	 */
	private int fuid;
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
	 * 购买数量
	 */
	private int number;
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
	 * 状态:0-未使用 1-已使用 2-已过期
	 */
	private int status;
	/**
	 * 商户订单号
	 */
	private String outTradeNo;
	/**
	 * 创建时间
	 */
	private Date createTime;

}
