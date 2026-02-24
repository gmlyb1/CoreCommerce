package com.CoreCommerce.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.CoreCommerce.domain.Popup;

@Mapper
public interface PopupRepository {

	 	void save(Popup popup);

	    List<Popup> findAll();

	    Popup findById(Long id);

	    void update(Popup popup);

	    void delete(Long id);

	    Popup findActivePopup();

		int countAll();

		List<Popup> findPaging(@Param("offset") int offset,
			      			   @Param("size") int size);
}
