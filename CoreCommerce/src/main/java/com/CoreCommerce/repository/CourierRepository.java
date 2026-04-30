package com.CoreCommerce.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.CoreCommerce.domain.Courier;

@Mapper
public interface CourierRepository {

	List<Courier> selectCourierList(@Param("offset") int offset, @Param("size") int size);

	List<Courier> selectCourierListAll();
	
	int countFindAll();

	void insertCourier(Courier courier);

	void deleteCourier(Long courier_no);


}
