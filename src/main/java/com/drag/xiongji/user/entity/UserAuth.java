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
 * 会员权限表
 * @author longyunbo
 *
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "t_user_auth")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class UserAuth implements Serializable {

	private static final long serialVersionUID = 5340596463953571810L;
	/**
	 * id
	 */
	@Id
	private int id;
	/**
	 * 权限名称
	 */
	private String authName;
	/**
	 * 创建时间
	 */
	private Date createTime;
}
