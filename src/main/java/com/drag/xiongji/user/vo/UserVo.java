package com.drag.xiongji.user.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Id;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class UserVo implements Serializable {

	private static final long serialVersionUID = -2779393742361373652L;
	/**
	 * id
	 */
	@Id
	private int id;
	/**
	 * openid
	 */
	private String openid;
	/**
	 * 是否团长 0-不是 1-是
	 */
	private int isHeader;
	/**
	 * 头像
	 */
	private String avatar;
	/**
	 * 性别
	 */
	private int sex;
	/**
	 * 昵称
	 */
	private String nickname;
	/**
	 * 真实姓名
	 */
	private String realname;
	/**
	 * 手机
	 */
	private String mobile;
	/**
	 * 生日
	 */
	private String birthday;
	/**
	 * 会员等级
	 */
	private int rankLevel;
	/**
	 * 恐龙骨
	 */
	private int dragBone;
	/**
	 * 经验值
	 */
	private int exp;
	/**
	 * 积分
	 */
	private String score;
	/**
	 * 砍价价格（砍价用到的字段）
	 */
	private BigDecimal price;
	/**
	 * 购买数量(秒杀用到的字段)
	 */
	private int number;
	/**
	 * 拼团，砍价，助力编号
	 */
	private String code;
	/**
	 * 拼团状态0:未付款 1:拼团中，2:拼团成功, 3：拼团失败,
	 */
	private int status;
	/**
	 * 创建时间
	 */
	private String createTime;
	/**
	 * 更新时间
	 */
	private String updateTime;
	
}
