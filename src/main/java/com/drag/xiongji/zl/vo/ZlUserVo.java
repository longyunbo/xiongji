package com.drag.xiongji.zl.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Id;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper=false)
public class ZlUserVo implements Serializable {

	private static final long serialVersionUID = -4663045863940661616L;
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
	 * 助力商品id
	 */
	private BigDecimal zlgoodsId;
	/**
	 * 砍价商品名称
	 */
	private String zlgoodsName;
	/**
	 * 助力编号
	 */
	private int zlcode;
	/**
	 * 砍价金额
	 */
	private BigDecimal zlPrice;
	/**
	 * 助力人数
	 */
	private int zlnumber;
	/**
	 * 创建日期
	 */
	private Date addtime;
	/**
	 * 结束时间
	 */
	private Date endtime;
	/**
	 * 0:未付款 1:助力中，2:助力成功, 3：助力失败
	 */
	private int zlstatus;
	

}
