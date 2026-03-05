package com.CoreCommerce.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.CoreCommerce.domain.SalesStats;

@Mapper
public interface DashboardRepository {

	int countMember();

	int countOrder();

	int todaySales();

	int totalSales();

	List<SalesStats> productSalesStats();
	List<SalesStats> orderStatusStats();
	List<SalesStats> dailySalesStats();
	List<SalesStats> monthlySalesStats();

}
