package com.CoreCommerce.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.CoreCommerce.domain.Banner;
import com.CoreCommerce.repository.BannerRepository;

@Service
public class BannerService {

	private final BannerRepository bannerRepository;
	
	public BannerService(BannerRepository bannerRepository) {
	    this.bannerRepository = bannerRepository;
	}

	public List<Banner> getAllBanners() {
		return bannerRepository.getAllBanners();
	}

	public void saveBanner(Banner banner) {
		bannerRepository.saveBanner(banner);
	}

	public void deleteBanner(Long id) {
		bannerRepository.deleteBanner(id);
	}

	public Banner getBannerById(Long id) {
		return bannerRepository.getBannerById(id);
	}

	public void updateBanner(Banner banner) {
		bannerRepository.updateBanner(banner);
	}

	public int countAll() {
		return bannerRepository.countAll();
	}

	public List<Banner> findPaging(int offset, int size) {
		return bannerRepository.findPaging(offset,size);
	}
	
}
