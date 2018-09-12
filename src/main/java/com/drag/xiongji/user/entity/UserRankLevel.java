package com.drag.xiongji.user.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
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
 * 会员等级表
 * @author longyunbo
 *
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "t_user_rank_level")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class UserRankLevel implements Serializable {

	private static final long serialVersionUID = -1565944941268183549L;
	/**
	 * id
	 */
	@Id
	private int id;
	/**
	 * 等级
	 */
	private int level;
	/**
	 * 等级名称
	 */
	private String levelName;
	/**
	 * 所需经验
	 */
	private int exp;
	/**
	 * 权限
	 */
	private String auth;
	/**
	 * 创建时间
	 */
	private Date createTime;

}
