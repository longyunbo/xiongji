package com.drag.xiongji.common;


public final class Constant {
	//成功
	public static final String SUCCESS = "0000";
	//失败
	public static final String FAIL = "9999";
	public final static String fomat = "yyyy-MM-dd HH:mm:ss";
	public final static String fomatNo = "yyyy-MM-dd";
	public final static int size = 2000;
	/**
	 * 活动类型
	 */
	public final static String TYPE_PT = "pt";
	public final static String TYPE_MS = "ms";
	public final static String TYPE_KJ = "kj";
	public final static String TYPE_ZL = "zl";
	public final static String TYPE_DR = "drag";
	public final static String TYPE_LPK = "lpk";
	
	//消息发送状态
	public final static int SENDSTATUS_SUCC = 1;
	public final static int SENDSTATUS_FAIL = 2;

	//用户不存在
	public static final String USERNOTEXISTS = "9001";
	//商品不存在
	public static final String PRODUCTNOTEXISTS = "9002";
	//商品库存不足
	public static final String STOCK_FAIL = "9003";
	//订单不存在
	public static final String ORDERNOTEXISTS = "9002";
	//卡券不存在
	public static final String TICKETNOTEXISTS = "9004";
	//卡券已经被核销
	public static final String TICKET_DESTORY = "9005";
	//卡券已经过期
	public static final String TICKET_OVER = "9006";
	//用户权限不够
	public static final String AUTH_OVER = "9007";
	//用户信息不完整
	public static final String USERINFO_OVER = "9008";
	//用户积分不足
	public static final String SCORE_NOTENOUGH = "9009";
	//用户余额不足
	public static final String MONEY_NOTENOUGH = "9010";
	//用户充值异常
	public static final String RECHARGE_ERROR = "9011";
	//活动不存在
	public static final String ACTIVITYNOTEXISTS = "9012";
	//活动已结束
	public static final String ACTIVITYALREADYEND_FAIL = "9013";
	//活动已完成
	public static final String ACTIVITYALREADYDOWN_FAIL = "9014";
	//用户已参加活动
	public static final String USERALREADYIN_FAIL = "9015";
	//用户砍价次数超限
	public static final String KJTIME_OVER = "9016";
	
	
}
