package com.base.basicjwt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BasicjwtApplication {

	public static void main(String[] args) {
		SpringApplication.run(BasicjwtApplication.class, args);
	}

}
