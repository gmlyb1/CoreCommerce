package com.CoreCommerce.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com")
@MapperScan("com.CoreCommerce.repository")
public class CoreCommerceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoreCommerceApplication.class, args);
	}

}
