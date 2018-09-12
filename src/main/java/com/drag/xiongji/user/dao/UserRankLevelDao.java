package com.drag.xiongji.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.drag.xiongji.user.entity.UserRankLevel;

public interface UserRankLevelDao extends JpaRepository<UserRankLevel, String>, JpaSpecificationExecutor<UserRankLevel> {
	
	@Query(value = "select * from t_user_rank_level where level = ?1", nativeQuery = true)
	UserRankLevel findByLevel(int level);

}
