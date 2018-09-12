package com.drag.xiongji.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.drag.xiongji.user.entity.UserTicketRecord;


public interface UserTicketRecordDao extends JpaRepository<UserTicketRecord, String>, JpaSpecificationExecutor<UserTicketRecord> {
	
	
	
}
