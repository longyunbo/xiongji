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
@Table(name = "t_user_ticket_detail")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class UserTicketDetail implements Serializable {

	private static final long serialVersionUID = 9040973201457873437L;
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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
	private Date createTime;

}
