package com.drag.xiongji;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;


@ComponentScan(basePackages = { "com.drag.xiongji"})
@EnableJpaRepositories
@SpringBootApplication
@EnableScheduling
public class XiongJiApplication {

	public static void main(String[] args) {
		SpringApplication.run(XiongJiApplication.class, args);
	}
}
