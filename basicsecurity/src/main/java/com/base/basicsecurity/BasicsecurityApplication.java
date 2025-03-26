package com.base.basicsecurity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BasicsecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(BasicsecurityApplication.class, args);
	}

}
