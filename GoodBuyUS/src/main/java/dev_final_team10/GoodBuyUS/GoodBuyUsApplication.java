package dev_final_team10.GoodBuyUS;

import dev_final_team10.GoodBuyUS.service.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
<<<<<<< HEAD
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
=======
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.boot.autoconfigure.domain.EntityScan;
>>>>>>> 4b33e72e9dff2444fc150ba18dd6a79438e01b84

@EnableScheduling
@SpringBootApplication
<<<<<<< HEAD
@EnableJpaAuditing
=======
@EntityScan(basePackages = "dev_final_team10.GoodBuyUS.domain") // feature/main_payment의 EntityScan 추가
>>>>>>> 4b33e72e9dff2444fc150ba18dd6a79438e01b84
public class GoodBuyUsApplication implements CommandLineRunner {

	@Autowired
	private ExcelService excelService;

	public static void main(String[] args) {
		SpringApplication.run(GoodBuyUsApplication.class, args);
	}

	// 애플리케이션 실행 시 자동 실행되도록 설정
	@Override
	public void run(String... args) throws Exception {
		excelService.readExcelFileAndSaveToDatabase("code.xlsx");
	}
}
