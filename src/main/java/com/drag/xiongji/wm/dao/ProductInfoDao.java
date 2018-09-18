package com.drag.xiongji.wm.dao;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.drag.xiongji.wm.entity.ProductInfo;



public interface ProductInfoDao extends JpaRepository<ProductInfo, String>, JpaSpecificationExecutor<ProductInfo> {
	
	List<ProductInfo> findByIsEnd(int isEnd);
	
	@Query(value = "select * from t_product_info where goods_id = ?1", nativeQuery = true)
	ProductInfo findGoodsDetail(int goodsId);
	
	@Query(value = "select * from t_product_info where goods_id in (?1)", nativeQuery = true)
	List<ProductInfo> findByIdIn(Set<Integer> msIds);
	
	
	@Query(value = "select * from t_product_info where goods_name like %?1% ", nativeQuery = true)
	List<ProductInfo> findByNameLike(String name);
	
	@Modifying
	@Query(value = "UPDATE t_product_info set succ_times = succ_times + 1  where goods_id in(?1)",nativeQuery = true)
	public void updateSuccTimes(Set<Integer> ids);
	
	List<ProductInfo> findByType(String type);
	List<ProductInfo> findByTypeOrderByPriceDesc(String type);
	List<ProductInfo> findByTypeOrderByPriceAsc(String type);
	List<ProductInfo> findByTypeOrderBySuccTimesDesc(String type);
	
}
