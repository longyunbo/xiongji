package com.drag.xiongji.dwd.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Component
public class SignUtil {
	
	private static String dianwodaurl;
	private static String key;
	private static String secret;
	
	@Value("${dianwoda.url.dianwodaurl}")
    public void setUrl(String value) {
		dianwodaurl = value;
    }
	@Value("${dianwoda.url.key}")
    public void setKey(String value) {
		key = value;
    }
	@Value("${dianwoda.url.secret}")
    public void setSecret(String value) {
		secret = value;
    }
	
	/**
	 * 获取公共参数
	 * 
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static String getComParam(String url,Map<String, Object> params) throws NoSuchAlgorithmException {
		Date timestamp = new Date(System.currentTimeMillis());
		String sign = SignUtil.getSign(params, secret);
		String requestUrl = String.format("%s/%s?pk=%s&timestamp=%s&format=%s&sig=%s",dianwodaurl,url,key, timestamp.getTime(),"json", sign);
		return requestUrl;
	}
    
    /**
     * 获取签名
     * @return
     * @throws NoSuchAlgorithmException 
     */
    public static String getSign(Map<String, Object> params,String secretKey) throws NoSuchAlgorithmException{
    	String sign = "";
		StringBuilder sortedParams = new StringBuilder();
		Map<String, Object> resultMap = sortMapByKey(params); 
		resultMap.entrySet().stream().forEachOrdered(paramEntry -> sortedParams.append(paramEntry.getKey()).append(paramEntry.getValue()));
		try {
			log.info("【sortedParams】:{}",secretKey + sortedParams.toString() + secretKey);
			sign = SignUtil.getSign(secretKey + sortedParams.toString() + secretKey);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return sign;
    }
    
    /**
     * 使用 Map按key进行排序
     * @param map
     * @return
     */
    public static Map<String, Object> sortMapByKey(Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }

        Map<String, Object> sortMap = new TreeMap<String, Object>(
                new MapKeyComparator());

        sortMap.putAll(map);

        return sortMap;
    }
    
	/**
	 * @Description: SHA1加密字符串
	 * @param
	 * @return String
	 * @throws NoSuchAlgorithmException
	 */
	public static String getSign(String sortedParams) throws NoSuchAlgorithmException {
		MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
		messageDigest.update(sortedParams.getBytes());
		byte byteBuffer[] = messageDigest.digest();
		StringBuffer strHexString = new StringBuffer();
		for (int i = 0; i < byteBuffer.length; i++) {
			String hex = Integer.toHexString(0xff & byteBuffer[i]);
			if (hex.length() == 1) {
				strHexString.append('0');
			}
			strHexString.append(hex);
		}
		// 得到返回結果
		String SHA256Sign = strHexString.toString().toUpperCase();
		return SHA256Sign;
	}
}
