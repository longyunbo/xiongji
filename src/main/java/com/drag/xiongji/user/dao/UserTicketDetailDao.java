package com.drag.xiongji.user.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.drag.xiongji.user.entity.UserTicketDetail;


public interface UserTicketDetailDao extends JpaRepository<UserTicketDetail, String>, JpaSpecificationExecutor<UserTicketDetail> {
	
	@Query(value = "select * from t_user_ticket_detail where id = ?1", nativeQuery = true)
	UserTicketDetail findOne(int id);
	
	@Query(value = "select * from t_user_ticket_detail where ticket_id = ?1 order by create_time desc", nativeQuery = true)
	List<UserTicketDetail> findByTicketId(int ticketid);
	
	@Query(value = "select * from t_user_ticket_detail where uid = ?1 order by create_time desc", nativeQuery = true)
	List<UserTicketDetail> findByUid(int uid);
	
	@Query(value = "select * from t_user_ticket_detail where id in (?1)", nativeQuery = true)
	List<UserTicketDetail> findByIdIn(List<Integer> ids);
	
}
