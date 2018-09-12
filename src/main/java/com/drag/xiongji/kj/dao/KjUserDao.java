package com.drag.xiongji.kj.dao;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.drag.xiongji.kj.entity.KjUser;


public interface KjUserDao extends JpaRepository<KjUser, String>, JpaSpecificationExecutor<KjUser> {
	
	
	@Query(value = "select * from kj_user where kjgoods_id = ?1", nativeQuery = true)
	List<KjUser> findByKjGoodsId(int goodsId);
	
	@Query(value = "select * from kj_user where kjgoods_id = ?1 and kjstatus = ?2", nativeQuery = true)
	List<KjUser> findByKjGoodsIdAndKjstatus(int goodsId,int kjstatus);
	
	@Query(value = "select * from kj_user where kjgoods_id = ?1 and uid = ?2 and is_header = ?3", nativeQuery = true)
	List<KjUser> findByKjGoodsIdAndUidAndIsHeader(int goodsId,int uid,int isHead);
	
	@Query(value = "select * from kj_user where kjgoods_id = ?1 and kjstatus = ?2 and is_header = ?3", nativeQuery = true)
	List<KjUser> findByKjGoodsIdAndKjstatusAndIsHeader(int goodsId,int kjstatus,int isHead);
	
	@Query(value = "select * from kj_user where kjgoods_id = ?1 and is_header = ?2 order by create_time desc", nativeQuery = true)
	List<KjUser> findByKjGoodsIdAndIsHead(int goodsId,int isHead);
	
	@Query(value = "select * from kj_user where uid = ?1 order by create_time desc", nativeQuery = true)
	List<KjUser> findByUid(int uid);
	
	@Query(value = "select * from kj_user where uid = ?1 and kjgoods_id = ?2 and is_header = ?3 and kjstatus = ?4 order by create_time desc", nativeQuery = true)
	List<KjUser> findByUidAndKjgoodsIdAndIsHeadAndKjStatus(int uid,int goodsId,int isHead,int kjstatus);
	
	@Query(value = "select * from kj_user where kjcode = ?1 order by create_time desc", nativeQuery = true)
	List<KjUser> findByKjCode(String kjCode);
	
	@Query(value = "select * from kj_user where kjcode in (?1) order by create_time desc", nativeQuery = true)
	List<KjUser> findByKjCodeIn(Set<String> kjCodes);
	
	@Query(value = "select * from kj_user where kjcode in (?1) and is_header = 1 order by create_time desc", nativeQuery = true)
	List<KjUser> findByKjCodeInAndHead(Set<String> kjCodes);
	
	@Query(value = "select * from kj_user where kjcode = ?1 and is_header = ?2 order by create_time desc", nativeQuery = true)
	List<KjUser> findByKjCodeAndIsHead(String kjCode,int isHead);
	
	@Query(value = "select * from kj_user where kjgoods_id = ?1 and is_header = ?2 and kjcode = ?3 order by create_time desc", nativeQuery = true)
	List<KjUser> findByKjGoodsIdAndIsHeadAndKjCode(int goodsId,int isHead,String kjCode);
	
	@Query(value = "select * from kj_user where kjgoods_id = ?1 and uid = ?2 and kjcode = ?3", nativeQuery = true)
	KjUser findByKjGoodsIdAndUidAndKjCode(int goodsId,int uid,String kjCode);
	
	@Modifying
	@Query(value = "UPDATE kj_user set kjstatus = 2 where kjcode =?1",nativeQuery = true)
	public int updateKjstatus(String kjcode);
}
