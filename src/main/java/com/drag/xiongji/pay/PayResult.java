package com.drag.xiongji.pay;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.drag.xiongji.pay.common.StreamUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 接收支付结果
 */
@Slf4j
public class PayResult{
	private static final Logger L = Logger.getLogger(PayResult.class);
       
	public static String payResult(HttpServletRequest request) {
		try {
			String reqParams = StreamUtil.read(request.getInputStream());
			log.info("-------支付结果:" + reqParams);
			StringBuffer sb = new StringBuffer("<xml><return_code>SUCCESS</return_code><return_msg>OK</return_msg></xml>");
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("-------", e);
		}
		return "";
	}

}
