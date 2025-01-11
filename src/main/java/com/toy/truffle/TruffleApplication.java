package com.toy.truffle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class TruffleApplication {

	public static void main(String[] args) {
		SpringApplication.run(TruffleApplication.class, args);
	}

}
