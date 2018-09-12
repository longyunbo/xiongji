package com.drag.xiongji.user.form;

import lombok.Data;

@Data
public class UserForm {
	
	/**
	 * openid
	 */
	private String openid;
	/**
	 * 头像
	 */
	private String avatar;
	/**
	 * 昵称
	 */
	private String nickname;
	/**
	 * 姓名
	 */
	private String realname;
	
	/**
	 * 性别：0-女，1-男
	 */
	private int sex;
	/**
	 * 生日
	 */
	private String birthday;
	/**
	 * 手机
	 */
	private String mobile;
	
}
