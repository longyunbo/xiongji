package com.drag.xiongji.ms.entity;

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

@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "ms_remind")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class MsRemind implements Serializable {
	private static final long serialVersionUID = -2426138601425128432L;
	
	// 开抢状态,0-开抢提醒,1-取消提醒
	public final static int STATUS_YES = 0;
	public final static int STATUS_NO = 1;
	
	/**
	 * 自增id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	/**
	 * 秒杀商品id
	 */
	private int msgoodsId;
	/**
	 * 用户id
	 */
	private String openid;
	/**
	 * 商品名称
	 */
	private String msgoodsName;
	/**
	 * 创建日期
	 */
	private Date createTime;
	/**
	 * 状态,0-开抢提醒,1-取消提醒
	 */
	private int status;
	/**
	 * 消息发送状态:0-未发送,1-已发送,2-发送失败
	 */
	private int sendstatus;
	/**
	 * formId
	 */
	private String formId;
	

}
