package com.drag.xiongji.user.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.drag.xiongji.user.entity.UserDragRecord;


public interface UserDragRecordDao extends JpaRepository<UserDragRecord, String>, JpaSpecificationExecutor<UserDragRecord> {
	
	
	@Query(value = "select * from t_user_drag_record where uid = ?1", nativeQuery = true)
	UserDragRecord findGoodsDetail(int uid);
	
	
	@Query(value = "select * from t_user_drag_record where uid = ?1 and type = ?2", nativeQuery = true)
	List<UserDragRecord> findByUidAndType(int uid,String type);
	
	@Query(value = "select * from t_user_drag_record where uid = ?1", nativeQuery = true)
	List<UserDragRecord> findByUid(int uid);
	
}
