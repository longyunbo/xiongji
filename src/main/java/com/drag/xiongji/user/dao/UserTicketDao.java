package com.drag.xiongji.user.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.drag.xiongji.user.entity.UserTicket;


public interface UserTicketDao extends JpaRepository<UserTicket, String>, JpaSpecificationExecutor<UserTicket> {
	
	
	@Query(value = "select * from t_user_ticket where id = ?1", nativeQuery = true)
	UserTicket findOne(int id);
	
	
	@Query(value = "select * from t_user_ticket where uid = ?1 and goods_id = ?2 and type = ?3 order by create_time desc", nativeQuery = true)
	List<UserTicket> findByUidAndGoodsIdAndType(int uid,int goodsId,String type);
	
	@Query(value = "select * from t_user_ticket where status = ?1 order by create_time desc", nativeQuery = true)
	List<UserTicket> findByStatus(int status);
	
	
	@Query(value = "select * from t_user_ticket where uid = ?1 and status = ?2 order by create_time desc", nativeQuery = true)
	List<UserTicket> findByUidAndType(int uid,String status);
	
	@Query(value = "select * from t_user_ticket where uid = ?1 order by create_time desc", nativeQuery = true)
	List<UserTicket> findByUid(int uid);
	
}
