package com.drag.xiongji.kj.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Id;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper=false)
public class KjUserVo implements Serializable {

	private static final long serialVersionUID = -4418294114341337765L;
	/**
	 * 自增id
	 */
	@Id
	private int id;
	/**
	 * 用户id
	 */
	private String uid;
	/**
	 * 团长id
	 */
	private BigDecimal grouperId;
	/**
	 * 砍价商品id
	 */
	private BigDecimal kjgoodsId;
	/**
	 * 砍价商品名称
	 */
	private String kjgoodsName;
	/**
	 * 砍价编号
	 */
	private int kjcode;
	/**
	 * 砍价金额
	 */
	private BigDecimal kjPrice;
	/**
	 * 砍价人数
	 */
	private int kjnumber;
	/**
	 * 创建日期
	 */
	private Date addtime;
	/**
	 * 结束时间
	 */
	private Date endtime;
	/**
	 * 0:未付款 1:砍价中，2:砍价成功, 3：砍价失败
	 */
	private int kjstatus;
	

}
