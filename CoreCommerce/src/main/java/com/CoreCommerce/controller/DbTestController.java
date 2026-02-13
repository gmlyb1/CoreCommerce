package com.CoreCommerce.controller;

import java.sql.Connection;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DbTestController {

	 @Autowired(required = false)
    private DataSource dataSource;
	 

	    @GetMapping("/db-test")
	    public String dbTest() {
	        try (Connection conn = dataSource.getConnection()) {
	            return "DB 연결 성공: " + conn.getMetaData().getURL();
	        } catch (Exception e) {
	            e.printStackTrace();
	            return "DB 연결 실패";
	        }
	    }
}
