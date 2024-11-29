package dev_final_team10.GoodBuyUS;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "dev_final_team10.GoodBuyUS.domain")
public class GoodBuyUsApplication {
	public static void main(String[] args) {
		SpringApplication.run(GoodBuyUsApplication.class, args);
	}
}

