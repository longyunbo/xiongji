package com.drag.xiongji.kj.dao;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.drag.xiongji.kj.entity.KjGoods;


public interface KjGoodsDao extends JpaRepository<KjGoods, String>, JpaSpecificationExecutor<KjGoods> {
	
	List<KjGoods> findByIsEnd(int isEnd);
	
	@Query(value = "select * from kj_goods where kjgoods_id = ?1", nativeQuery = true)
	KjGoods findGoodsDetail(int goodsId);
	
	
	@Query(value = "select * from kj_goods where kjgoods_id in (?1)", nativeQuery = true)
	List<KjGoods> findByIdIn(Set<Integer> kjIds);
	
}
