package com.drag.xiongji.ms.vo;

import javax.persistence.Id;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper=false)
public class MsRemindVo {
	
	/**
	 * 自增id
	 */
	@Id
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
	private String createTime;
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
