package com.drag.xiongji.ms.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.drag.xiongji.ms.entity.MsRemind;


public interface MsRemindDao extends JpaRepository<MsRemind, String>, JpaSpecificationExecutor<MsRemind> {
	
	
	@Query(value = "select * from ms_remind where msgoods_id = ?1 and openid =?2 order by create_time desc", nativeQuery = true)
	MsRemind findByMsgoodsIdAndOpenid(int msgoodsId,String openid);
	
	@Query(value = "select * from ms_remind where status = ?1 order by create_time desc", nativeQuery = true)
	List<MsRemind> findByStatus(int status);
	
	@Query(value = "select * from ms_remind where openid = ?1 order by create_time desc", nativeQuery = true)
	List<MsRemind> findByOpenid(String openid);
}
