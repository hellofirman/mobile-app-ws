package com.hellofirman.mobileappws;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.hellofirman.mobileappws.security.AppProperties;

@SpringBootApplication
public class MobileAppWsApplication extends SpringBootServletInitializer {

	@Override //manual overriding 
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(MobileAppWsApplication.class);
	}
	
	public static void main(String[] args) {
		SpringApplication.run(MobileAppWsApplication.class, args);
	}

	//for security
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	} 
	
	//for spring app context
	@Bean
	public SpringApplicationContext applicationContext() {
		return new SpringApplicationContext();
	}
	
	//for spring app properties
	@Bean(name="AppProperties")
	public AppProperties getAppProperties() {
		return new AppProperties();
	}
}

