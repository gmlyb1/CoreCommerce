package com.CoreCommerce.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.CoreCommerce.domain.SalesStats;
import com.CoreCommerce.repository.DashboardRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardService {

	private final DashboardRepository dashboardRepository;

	public int countMember() {
		return dashboardRepository.countMember();
	}

	public int countOrder() {
		return dashboardRepository.countOrder();
	}

	public int todaySales() {
		return dashboardRepository.todaySales();
	}

	public int totalSales() {
		return dashboardRepository.totalSales();
	}

	public List<SalesStats> productSalesStats() {
	    return dashboardRepository.productSalesStats();
	}

	public List<SalesStats> orderStatusStats() {
	    return dashboardRepository.orderStatusStats();
	}

	public List<SalesStats> dailySalesStats() {
	    return dashboardRepository.dailySalesStats();
	}

	public List<SalesStats> monthlySalesStats() {
	    return dashboardRepository.monthlySalesStats();
	}

}
