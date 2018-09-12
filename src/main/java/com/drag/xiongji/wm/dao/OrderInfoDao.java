package com.drag.xiongji.wm.dao;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.drag.xiongji.wm.entity.OrderInfo;



public interface OrderInfoDao extends JpaRepository<OrderInfo, String>, JpaSpecificationExecutor<OrderInfo> {
	
	
	@Query(value = "select * from t_order_info where orderid = ?1", nativeQuery = true)
	OrderInfo findByOrderId(String orderid);
	
	@Query(value = "select * from t_order_info where orderid in (?1)", nativeQuery = true)
	List<OrderInfo> findByIdIn(Set<Integer> msIds);
	
	@Query(value = "select * from t_order_info where type = ?1", nativeQuery = true)
	List<OrderInfo> findByType(String type);
	
	@Query(value = "select * from t_order_info where uid = ?1", nativeQuery = true)
	List<OrderInfo> findByUid(int uid);
	
	@Query(value = "select * from t_order_info where uid = ?1 and type = ?2", nativeQuery = true)
	List<OrderInfo> findByUidAndType(int uid,String type);
	
	@Query(value = "select * from t_order_info where uid = ?1 and is_billing = 1", nativeQuery = true)
	List<OrderInfo> findByUidAndBill(int uid);
}
