package com.CoreCommerce.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.CoreCommerce.domain.Banner;

@Mapper
public interface BannerRepository {

	List<Banner> getAllBanners();

	void saveBanner(Banner banner);

	void deleteBanner(Long id);

	Banner getBannerById(Long id);

	void updateBanner(Banner banner);

	int countAll();

	List<Banner> findPaging(@Param("offset") int offset,
			      			   @Param("size") int size);


}
