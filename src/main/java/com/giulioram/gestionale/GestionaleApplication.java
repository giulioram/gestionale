package com.giulioram.gestionale;

import com.giulioram.gestionale.event.utils.Snowflake;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GestionaleApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestionaleApplication.class, args);
	}

	@Bean
	public Snowflake snowflake() {
		return new Snowflake(1, 1);
	}
}
