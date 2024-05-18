package com.vathevor.project.linklake;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class LinkLakeApplication {

	public static void main(String[] args) {
		SpringApplication.run(LinkLakeApplication.class, args);
	}

}
