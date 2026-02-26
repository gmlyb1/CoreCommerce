package com.CoreCommerce.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.CoreCommerce.domain.VisitLog;

@Mapper
public interface VisitLogRepository {

	List<VisitLog> findAll(@Param("offset") int offset,  @Param("size") int size);

	int countAll();

	void insert(VisitLog log);

}
