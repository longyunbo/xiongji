package com.drag.xiongji.pt.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Id;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper=false)
public class PtUserVo implements Serializable {

	private static final long serialVersionUID = 2717240804084601537L;
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
	 * 拼团商品id
	 */
	private BigDecimal ptgoodsId;
	/**
	 * 拼团编号
	 */
	private int ptcode;
	/**
	 * 拼团人数
	 */
	private int ptnumber;
	/**
	 * 创建日期
	 */
	private Date addtime;
	/**
	 * 结束时间
	 */
	private Date endtime;
	/**
	 * 0:未付款 1:拼团中，2:拼团成功, 3：拼团失败
	 */
	private int ptstatus;
	

}
