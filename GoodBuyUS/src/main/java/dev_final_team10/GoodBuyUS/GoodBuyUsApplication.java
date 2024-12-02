package dev_final_team10.GoodBuyUS;

import dev_final_team10.GoodBuyUS.service.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@EnableScheduling
@SpringBootApplication
@EntityScan(basePackages = "dev_final_team10.GoodBuyUS.domain") // feature/main_payment의 EntityScan 추가
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
