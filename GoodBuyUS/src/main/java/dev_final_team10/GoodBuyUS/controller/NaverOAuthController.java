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
    @RequestMapping("/users/sociallogin")
    public void redirectToNaverLogin(HttpServletResponse response) throws IOException {
        String clientId = "WMviEjPZFWlKSGcKakso"; // 네이버 클라이언트 ID
        String redirectUri = "http://15.164.5.135:8080/naver/callback"; // 네이버 인증 완료 후 리다이렉트될 URI
        String state = "randomStateValue"; // 임의의 문자열 (CSRF 방지)

        // 네이버 인증 URL 생성
        String naverLoginUrl = "https://nid.naver.com/oauth2.0/authorize"
                + "?response_type=code"
                + "&client_id=" + clientId
                + "&redirect_uri=" + redirectUri
                + "&state=" + state;

        // 네이버 로그인 페이지로 리다이렉트
        response.sendRedirect(naverLoginUrl);
    }

    /**
     * 네이버 콜백 처리
     * 네이버에서 인증이 완료되면 전달받은 code와 state를 사용해 JWT를 발급합니다.
     */
    @GetMapping("/naver/callback")
    public ResponseEntity<String> naverCallback(
            @RequestParam String code,
            @RequestParam String state,
            HttpServletResponse response) {
        try {
            naverOAuthService.processNaverLogin(code, state, response);
            return ResponseEntity.ok("네이버 로그인 성공! 헤더에서 토큰을 확인하세요.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("네이버 로그인 처리 중 오류가 발생했습니다.");
        }
    }


}
