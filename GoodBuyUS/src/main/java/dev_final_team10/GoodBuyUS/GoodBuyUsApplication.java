package dev_final_team10.GoodBuyUS;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class GoodBuyUsApplication {

	public static void main(String[] args) {
		SpringApplication.run(GoodBuyUsApplication.class, args);
	}

}
