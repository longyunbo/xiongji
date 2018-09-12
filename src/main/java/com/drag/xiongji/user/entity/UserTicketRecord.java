package com.drag.xiongji.user.entity;

import java.io.Serializable;
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
 * 会员卡券记录表
 * @author longyunbo
 *
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "t_user_ticket_record")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class UserTicketRecord implements Serializable {

	private static final long serialVersionUID = 6977036227599506108L;
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
	 * 卡券编号
	 */
	private int ticketId;
	/**
	 * 卡券使用数量
	 */
	private int number;
	/**
	 * 创建时间
	 */
	private Date createTime;

}
