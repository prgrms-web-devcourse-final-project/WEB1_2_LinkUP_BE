package dev_final_team10.GoodBuyUS.service;

import dev_final_team10.GoodBuyUS.domain.user.entity.Neighborhood;
import dev_final_team10.GoodBuyUS.repository.NeighborhoodRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

@Service
@RequiredArgsConstructor

public class ExcelService {
    private final NeighborhoodRepository neighborhoodRepository;

    public void readExcelFileAndSaveToDatabase(String filePath) throws IOException {

        // ClassPathResource로 파일을 읽기
        Resource resource = new ClassPathResource(filePath);
        InputStream fis = resource.getInputStream();

        // 엑셀 파일을 읽기 위한 Workbook 객체 생성
        Workbook workbook = new XSSFWorkbook(fis);

        // 첫 번째 시트 가져오기
        Sheet sheet = workbook.getSheetAt(0);

        //시트의 행 읽기
        Iterator<Row> rowIterator = sheet.iterator();

        // 첫 번째 행을 건너뛰고 두 번째 행부터 데이터 처리 (칼럼의 이름 행이므로)
        rowIterator.next();

        //다음 행이 없을 때까지 읽기
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();

            // 각 열의 데이터를 읽고 처리 (첫 번째 열: 시군구 명, 두 번째 열: 시군구 코드)
            String areaName = row.getCell(0).getStringCellValue();
            Double code = row.getCell(1).getNumericCellValue();

            // MySQL에 저장하는 로직
            Neighborhood neighborhood = Neighborhood.builder()
                    .neighborhoodCode(code)
                    .neighborhoodName(areaName)
                    .build();

            neighborhoodRepository.save(neighborhood);
        }
        //자원 해제
        workbook.close();
        fis.close();
    }
}

