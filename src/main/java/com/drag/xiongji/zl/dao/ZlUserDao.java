package com.drag.xiongji.zl.dao;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.drag.xiongji.zl.entity.ZlUser;


public interface ZlUserDao extends JpaRepository<ZlUser, String>, JpaSpecificationExecutor<ZlUser> {
	
	
	@Query(value = "select * from zl_user where zlgoods_id = ?1", nativeQuery = true)
	List<ZlUser> findByZlGoodsId(int goodsId);
	
	@Query(value = "select * from zl_user where zlgoods_id = ?1 and zlstatus = ?2", nativeQuery = true)
	List<ZlUser> findByZlGoodsIdAndZlstatus(int goodsId,int zlstatus);
	
	@Query(value = "select * from zl_user where zlgoods_id = ?1 and uid = ?2 and is_header = ?3", nativeQuery = true)
	List<ZlUser> findByZlGoodsIdAndUidAndIsHeader(int goodsId,int uid,int isHead);
	
	@Query(value = "select * from zl_user where zlgoods_id = ?1 and zlstatus = ?2 and is_header = ?3", nativeQuery = true)
	List<ZlUser> findByZlGoodsIdAndZlstatusAndIsHeader(int goodsId,int zlstatus,int isHead);
	
	@Query(value = "select * from zl_user where zlgoods_id = ?1 and is_header = ?2 order by create_time desc", nativeQuery = true)
	List<ZlUser> findByZlGoodsIdAndIsHead(int goodsId,int isHead);
	
	@Query(value = "select * from zl_user where uid = ?1 order by create_time desc", nativeQuery = true)
	List<ZlUser> findByUid(int uid);
	
	@Query(value = "select * from zl_user where uid = ?1 and zlgoods_id = ?2 and is_header = ?3 and zlstatus = ?4 order by create_time desc", nativeQuery = true)
	List<ZlUser> findByUidAndZlgoodsIdAndIsHeadAndZlstatus(int uid,int goodsId,int isHead,int zlstatus);
	
	@Query(value = "select * from zl_user where zlcode = ?1 order by create_time desc", nativeQuery = true)
	List<ZlUser> findByZlCode(String zlCode);
	
	@Query(value = "select * from zl_user where zlcode in (?1) order by create_time desc", nativeQuery = true)
	List<ZlUser> findByZlCodeIn(Set<String> zlcodes);
	
	@Query(value = "select * from zl_user where zlcode in (?1) and is_header = 1 order by create_time desc", nativeQuery = true)
	List<ZlUser> findByZlCodeInAndHead(Set<String> zlcodes);
	
	@Query(value = "select * from zl_user where zlcode = ?1 and is_header = ?2 order by create_time desc", nativeQuery = true)
	List<ZlUser> findByZlCodeAndIsHead(String zlCode,int isHead);
	
	@Query(value = "select * from zl_user where zlgoods_id = ?1 and is_header = ?2 and zlcode = ?3 order by create_time desc", nativeQuery = true)
	List<ZlUser> findByZlGoodsIdAndIsHeadAndZlCode(int goodsId,int isHead,String zlCode);
	
	@Query(value = "select * from zl_user where zlgoods_id = ?1 and uid = ?2 and zlcode = ?3", nativeQuery = true)
	ZlUser findByZlGoodsIdAndUidAndZlCode(int goodsId,int uid,String zlCode);
	
	@Modifying
	@Query(value = "UPDATE zl_user set zlstatus = 2 where zlcode =?1",nativeQuery = true)
	public int updateZlstatus(String zlcode);
}
