package com.huang;

import com.huang.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JdbcManagementApplication {

	// use jdbcTemplate
	public static void main(String[] args) {
		SpringApplication.run(JdbcManagementApplication.class, args);
	}
}
