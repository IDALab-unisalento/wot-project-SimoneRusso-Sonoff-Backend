package it.unisalento.sonoffbackend;

import java.util.Collections;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SonoffBackEndApplication {
	private static final Logger LOG = LogManager.getLogger(SonoffBackEndApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SonoffBackEndApplication.class, args);
	}

}
