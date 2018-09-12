package com.drag.xiongji.user.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.drag.xiongji.user.entity.UserDragUsedRecord;


public interface UserDragUsedRecordDao extends JpaRepository<UserDragUsedRecord, String>, JpaSpecificationExecutor<UserDragUsedRecord> {
	
	@Query(value = "select * from t_user_drag_used_record where uid = ?1 and type = ?2", nativeQuery = true)
	List<UserDragUsedRecord> findByUidAndType(int uid,String type);
	
	@Query(value = "select * from t_user_drag_used_record where uid = ?1", nativeQuery = true)
	List<UserDragUsedRecord> findByUid(int uid);
	
	
}
