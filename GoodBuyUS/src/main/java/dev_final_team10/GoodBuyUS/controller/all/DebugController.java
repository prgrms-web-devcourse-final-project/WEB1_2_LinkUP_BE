package dev_final_team10.GoodBuyUS.controller.all;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@RestController
@RequestMapping("/debug")
public class DebugController {

    @Value("${file.upload-dir}") // 설정 파일에서 경로 가져오기
    private String uploadDir;

    @GetMapping("/upload-path")
    public ResponseEntity<?> getUploadPath() {
        Path path = Paths.get(uploadDir);
        return ResponseEntity.ok().body(Map.of(
                "uploadDir", uploadDir,
                "absolutePath", path.toAbsolutePath().toString(),
                "exists", Files.exists(path) // 폴더가 실제 존재하는지 확인
        ));
    }
}
