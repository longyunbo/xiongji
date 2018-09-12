package com.drag.xiongji.user.vo;

import java.io.Serializable;

import javax.persistence.Id;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 收货地址表
 * @author longyunbo
 *
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class UserReceivingAddressVo implements Serializable {

	private static final long serialVersionUID = -6266058733506895808L;
	@Id
	private int id;
	/**
	 * 用户编号
	 */
	private int uid;
	/**
	 * 收货人
	 */
	private String receiptName;
	/**
	 * 联系方式
	 */
	private String receiptTel;
	/**
	 * 所在区域
	 */
	private String region;
	/**
	 * 邮政编码
	 */
	private String postalcode;
	/**
	 * 市级
	 */
	private String cityName;
	/**
	 * 详细地址
	 */
	private String receiptAddress;
	/**
	 * 创建时间
	 */
	private String createTime;
	/**
	 * 更新时间
	 */
	private String updateTime;

}
