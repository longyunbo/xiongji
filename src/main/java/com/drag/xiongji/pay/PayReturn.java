package com.drag.xiongji.pay;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.drag.xiongji.pay.common.CertHttpUtil;
import com.drag.xiongji.pay.common.Configure;
import com.drag.xiongji.pay.common.RandomStringGenerator;
import com.drag.xiongji.pay.common.Signature;
import com.drag.xiongji.pay.model.PayReturnInfo;
import com.drag.xiongji.pay.model.PayReturnResultInfo;
import com.drag.xiongji.utils.XStreamEx;
import com.thoughtworks.xstream.io.xml.DomDriver;

import lombok.extern.slf4j.Slf4j;

/**
 * 退款接口
 */
@Slf4j
@Service
public class PayReturn {
	private static final Logger L = Logger.getLogger(PayReturn.class);
	
	private static String p12path;
	@Value("${weixin.url.p12path}")
    public void setSecret(String value) {
		p12path = value;
    }

	public static JSONObject wxReturn(String out_trade_no,int price) {
		JSONObject json = new JSONObject();
		try {
//			File cfgFile = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "apiclient_cert.p12");
//			String certPath = cfgFile.getPath();
			String certPath = p12path;
			//随机生成的退款编号
			String out_refund_no = RandomStringGenerator.getRandomStringByLength(32);
			
			PayReturnInfo returnInfo = new PayReturnInfo();
			returnInfo.setAppid(Configure.appid);
			returnInfo.setMch_id(Configure.mch_id);
			returnInfo.setNonce_str(RandomStringGenerator.getRandomStringByLength(32));
			returnInfo.setOut_trade_no(out_trade_no);
			returnInfo.setOut_refund_no(out_refund_no);
			returnInfo.setTotal_fee(price);
			returnInfo.setRefund_fee(price);
			// 生成签名
			String sign = Signature.getSign(returnInfo);
			returnInfo.setSign(sign);

//			String result = HttpRequest.sendPost("https://api.mch.weixin.qq.com/secapi/pay/refund", returnInfo);
			String result = CertHttpUtil.postData("https://api.mch.weixin.qq.com/secapi/pay/refund", returnInfo, Configure.mch_id, certPath);
			System.out.println(result);
			log.info("---------退款返回:" + result);
			XStreamEx xStream = new XStreamEx(new DomDriver("utf-8"));
//			XStream xStream = new XStream();
			xStream.alias("xml", PayReturnResultInfo.class);
//			xStream.ignoreUnknownElements();
			PayReturnResultInfo payReturnResultInfo = (PayReturnResultInfo) xStream.fromXML(result);
			JSONObject itemJSONObj = JSONObject.parseObject(JSON.toJSONString(payReturnResultInfo));
			return itemJSONObj;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("-------", e);
		}
		return json;

	}

}
