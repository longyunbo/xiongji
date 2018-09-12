package com.drag.xiongji.wm.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.drag.xiongji.wm.entity.OrderComment;



public interface OrderCommentDao extends JpaRepository<OrderComment, String>, JpaSpecificationExecutor<OrderComment> {
	

	@Query(value = "select * from t_order_comment where goods_id = ?1", nativeQuery = true)
	List<OrderComment> findByGoodsId(int goodsId);
}
