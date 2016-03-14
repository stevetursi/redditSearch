package com.tursi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
public class RedditsearchApplication {

	public static void main(String[] args) {
		SpringApplication.run(RedditsearchApplication.class, args);
	}
}
