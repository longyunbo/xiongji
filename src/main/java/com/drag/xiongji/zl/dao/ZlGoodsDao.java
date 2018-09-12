package com.drag.xiongji.zl.dao;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.drag.xiongji.zl.entity.ZlGoods;


public interface ZlGoodsDao extends JpaRepository<ZlGoods, String>, JpaSpecificationExecutor<ZlGoods> {
	
	List<ZlGoods> findByIsEnd(int isEnd);
	
	@Query(value = "select * from zl_goods where zlgoods_id = ?1", nativeQuery = true)
	ZlGoods findGoodsDetail(int goodsId);
	
	@Query(value = "select * from zl_goods where zlgoods_id in (?1)", nativeQuery = true)
	List<ZlGoods> findByIdIn(Set<Integer> zlIds);
	
	
}
