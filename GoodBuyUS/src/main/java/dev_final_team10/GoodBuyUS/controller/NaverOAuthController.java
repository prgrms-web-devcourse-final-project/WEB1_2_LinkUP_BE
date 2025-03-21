package dev_final_team10.GoodBuyUS.controller;

import dev_final_team10.GoodBuyUS.service.NaverOAuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
@Slf4j
@RestController
@RequiredArgsConstructor
public class NaverOAuthController {

    private final NaverOAuthService naverOAuthService;

    /**
     * 네이버 로그인 페이지로 리다이렉트
     * 사용자가 "/users/sociallogin"을 호출하면 네이버 로그인 페이지로 이동합니다.
     */
    @GetMapping("/users/sociallogin")
    public ResponseEntity<String> redirectToNaverLogin(HttpServletResponse response) {
        String naverLoginUrl = naverOAuthService.generateNaverLoginUrl();
        return ResponseEntity.status(HttpStatus.FOUND).header("Location", naverLoginUrl).build();
    }

    /**
     * 네이버 로그인 콜백 처리
     */
    @GetMapping("/naver/callback")
    public void naverCallback(
            @RequestParam String code,
            @RequestParam String state,
            HttpServletResponse response) {
        try {
            // 네이버 로그인 처리 및 리다이렉트
            naverOAuthService.processNaverLogin(code, state, response);
        } catch (Exception e) {
            log.error("네이버 로그인 처리 중 오류 발생", e);
            try {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "로그인 처리 중 오류 발생");
            } catch (IOException ex) {
                log.error("에러 응답 전송 중 추가 오류 발생", ex);
            }
        }
    }
}
