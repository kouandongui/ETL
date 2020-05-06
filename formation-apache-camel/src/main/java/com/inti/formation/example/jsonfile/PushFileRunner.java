package com.inti.formation.example.jsonfile;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * 
 * @author  Sylvanius Kouandongui
 *
 */
@EnableAutoConfiguration
@SpringBootApplication
@ComponentScan
@Configuration
@EnableMongoRepositories
public class PushFileRunner {


	/**
	 * start the application
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(PushFileRunner.class, args);

	}

}
