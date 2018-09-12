package com.drag.xiongji.ms.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.drag.xiongji.ms.entity.MsOrder;


public interface MsOrderDao extends JpaRepository<MsOrder, String>, JpaSpecificationExecutor<MsOrder> {
	
	
	@Query(value = "select * from ms_order where uid = ?1", nativeQuery = true)
	List<MsOrder> findByUid(int uid);
	
	@Query(value = "select * from ms_order where msgoods_id = ?1 order by create_time desc", nativeQuery = true)
	List<MsOrder> findByMsgoodsId(int msgoodsId);
	
	@Query(value = "select * from ms_order where msgoods_id = ?1 and uid =?2 order by create_time desc", nativeQuery = true)
	List<MsOrder> findByMsgoodsIdAndUid(int msgoodsId,int uid);
}
