package cn.sparrowmini.pem.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@EntityScan("cn.sparrowmini.pem.model")
@SpringBootApplication
public class SparrowPemApplication {

	public static void main(String[] args) {
		SpringApplication.run(SparrowPemApplication.class, args);

	}

}
