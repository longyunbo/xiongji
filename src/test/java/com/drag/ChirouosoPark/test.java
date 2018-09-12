package com.drag.ChirouosoPark;

import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.TreeMap;

import com.drag.xiongji.dwd.util.SignUtil;

public class test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			Map<String, Object> params = new TreeMap<>();
			params.put("order_original_id", "200000356865713004");
			System.out.println(SignUtil.getComParam("api/v3/order-get.json",params));
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
