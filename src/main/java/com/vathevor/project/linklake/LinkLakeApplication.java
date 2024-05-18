package com.vathevor.project.linklake;

import com.vathevor.shared.spring.identity.UserIdResolverConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Import;

@Import({
		UserIdResolverConfig.class
})
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class LinkLakeApplication {

	public static void main(String[] args) {
		SpringApplication.run(LinkLakeApplication.class, args);
	}

}
