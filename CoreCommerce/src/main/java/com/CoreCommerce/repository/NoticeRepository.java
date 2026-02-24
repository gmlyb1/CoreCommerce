package com.CoreCommerce.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.CoreCommerce.domain.Notice;

@Mapper
public interface NoticeRepository {

	List<Notice> findAll();

	Notice findById(Long id);

	void insert(Notice notice);

	List<Notice> findPaged(@Param("offset") int offset,
            			   @Param("size") int size);

	int countAll();

	void update(Notice notice);

	void delete(Long id);

}
