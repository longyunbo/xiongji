package com.drag.xiongji.pay.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Component
public class Configure {
	public static String key = "你的商户的api秘钥";
	//小程序ID	
	public static String appid = "你的小程序id";
	//商户号
	public static String mch_id = "你的商户号";
	//
	public static String secret = "你的小程序的secret";

	@Value("${weixin.url.appid}")
    public void setAppid(String value) {
		appid = value;
    }
	@Value("${weixin.url.key}")
    public void setKey(String value) {
		key = value;
    }
	@Value("${weixin.url.mch_id}")
    public void setMchId(String value) {
		mch_id = value;
    }
	@Value("${weixin.url.secret}")
    public void setSecret(String value) {
		secret = value;
    }

}
