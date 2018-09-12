package com.drag.xiongji.user.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.drag.xiongji.user.entity.UserTicketTemplate;


public interface UserTicketTemplateDao extends JpaRepository<UserTicketTemplate, String>, JpaSpecificationExecutor<UserTicketTemplate> {
	
	
	@Query(value = "select * from t_user_ticket_template where goods_id = ?1 and type = ?2", nativeQuery = true)
	UserTicketTemplate findByGoodsIdAndType(int goodsId,String type);
	
	
	@Query(value = "select * from t_user_ticket_template where  type = ?1", nativeQuery = true)
	List<UserTicketTemplate> findByType(String type);
	
	
}
