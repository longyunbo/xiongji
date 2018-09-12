package com.drag.xiongji.pt.dao;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.drag.xiongji.pt.entity.PtGoods;


public interface PtGoodsDao extends JpaRepository<PtGoods, String>, JpaSpecificationExecutor<PtGoods> {
	
	List<PtGoods> findByIsEnd(int isEnd);
	
	@Query(value = "select * from pt_goods where ptgoods_id = ?1", nativeQuery = true)
	PtGoods findGoodsDetail(int goodsId);
	
	@Query(value = "select * from pt_goods where ptgoods_id in (?1)", nativeQuery = true)
	List<PtGoods> findByIdIn(Set<Integer> ptIds);
	
	
}
