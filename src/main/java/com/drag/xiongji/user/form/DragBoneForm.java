package com.drag.xiongji.user.form;

import lombok.Data;

@Data
public class DragBoneForm {
	
	/**
	 * 用户id
	 */
	private String openid;
	/**
	 * 商品编号
	 */
	private int goodsId;
	/**
	 * 类型:pt-拼团,ms-秒杀,zl-助力,kj-砍价,drag-恐龙骨
	 */
	private String type;
	/**
	 * 消耗恐龙骨
	 */
	private int dragBone;
	
	
	
}
