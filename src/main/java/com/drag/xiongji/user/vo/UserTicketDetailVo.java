package com.drag.xiongji.user.vo;

import java.math.BigDecimal;

import javax.persistence.Id;

import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 会员卡券表
 * @author longyunbo
 *
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class UserTicketDetailVo {
	
	
	@Id
	private int id;
	/**
	 * 类型
	 */
	private String type;
	/**
	 * 卡券编号
	 */
	private int ticketId;
	/**
	 * 用户编号
	 */
	private int uid;
	/**
	 * 商品编号
	 */
	private int goodsId;
	/**
	 * 商品名称
	 */
	private String goodsName;
	/**
	 * 商品图片
	 */
	private String goodsThumb;
	/**
	 * 购买规格
	 */
	private String norms;
	/**
	 * 商品数量
	 */
	private int number;
	/**
	 * 消耗金额
	 */
	private BigDecimal price;
	/**
	 * 创建时间
	 */
	private String createTime;

}
