package com.messenger.myperfectmessenger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
public class MyPerfectMessengerApplication {

	static final Logger log = LoggerFactory.getLogger(MyPerfectMessengerApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(MyPerfectMessengerApplication.class, args);
		log.info("After Starting application");
	}

}
