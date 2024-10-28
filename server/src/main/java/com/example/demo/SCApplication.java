package com.example.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.demo.mapper")
public class SCApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(SCApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println();
	}
}
