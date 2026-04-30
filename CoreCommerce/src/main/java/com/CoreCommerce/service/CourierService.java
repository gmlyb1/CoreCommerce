package com.CoreCommerce.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.CoreCommerce.domain.Courier;
import com.CoreCommerce.repository.CourierRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CourierService {

	private final CourierRepository courierRepository;
	
	public List<Courier> selectCourierList(int offset, int size) {
		return courierRepository.selectCourierList(offset, size);
	}
	
	public List<Courier> selectCourierListAll() {
		return courierRepository.selectCourierListAll();
	}
	
	public int countFindAll() {
		return courierRepository.countFindAll();
	}
	
	public void insertCourier(Courier courier) {
		courierRepository.insertCourier(courier);
	}
	
	public void deleteCourier(Long courier_no) {
		courierRepository.deleteCourier(courier_no);
	}
}
