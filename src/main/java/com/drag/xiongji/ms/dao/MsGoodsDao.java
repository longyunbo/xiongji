package com.drag.xiongji.ms.dao;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.drag.xiongji.ms.entity.MsGoods;


public interface MsGoodsDao extends JpaRepository<MsGoods, String>, JpaSpecificationExecutor<MsGoods> {
	
	List<MsGoods> findByIsEnd(int isEnd);
	
	@Query(value = "select * from ms_goods where msgoods_id = ?1", nativeQuery = true)
	MsGoods findGoodsDetail(int goodsId);
	
	@Query(value = "select * from ms_goods where msgoods_id in (?1)", nativeQuery = true)
	List<MsGoods> findByIdIn(Set<Integer> msIds);
}
