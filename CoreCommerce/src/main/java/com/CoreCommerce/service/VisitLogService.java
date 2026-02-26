package com.CoreCommerce.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.CoreCommerce.domain.VisitLog;
import com.CoreCommerce.repository.VisitLogRepository;

@Service
public class VisitLogService {

	private final VisitLogRepository visitLogRepository;
	
	public VisitLogService(VisitLogRepository visitLogRepository) {
	    this.visitLogRepository = visitLogRepository;
	}
	
	public List<VisitLog> findPaging(int offset,  int size) {
		return visitLogRepository.findAll(offset, size);
	}
	
	public int countAll() {
		return visitLogRepository.countAll();
	}
	
	public void insert(VisitLog log) {
		visitLogRepository.insert(log);
	}
}
