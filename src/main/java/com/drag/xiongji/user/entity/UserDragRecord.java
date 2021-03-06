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
 * 会员积分记录表
 * @author longyunbo
 *
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "t_user_drag_record")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class UserDragRecord implements Serializable {

	private static final long serialVersionUID = -5807402303998048344L;
	/**
	 * id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
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
	 * 类型:pt-拼团,ms-秒杀,zl-助力,kj-砍价,drag-恐龙骨
	 */
	private String type;
	/**
	 * 当前恐龙骨
	 */
	private int dragBone;
	/**
	 * 获得恐龙骨
	 */
	private int availableDragBone;
	/**
	 * 创建时间
	 */
	private Date createTime;

}
