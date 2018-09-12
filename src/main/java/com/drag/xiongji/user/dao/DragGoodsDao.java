package com.drag.xiongji.user.dao;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.drag.xiongji.user.entity.DragGoods;


public interface DragGoodsDao extends JpaRepository<DragGoods, String>, JpaSpecificationExecutor<DragGoods> {
	
	List<DragGoods> findByIsEnd(int isEnd);
	
	@Query(value = "select * from drag_goods where drgoods_id = ?1", nativeQuery = true)
	DragGoods findGoodsDetail(int goodsId);
	
	@Query(value = "select * from drag_goods where drgoods_id in (?1)", nativeQuery = true)
	List<DragGoods> findByIdIn(Set<Integer> drIds);
	
	
}
