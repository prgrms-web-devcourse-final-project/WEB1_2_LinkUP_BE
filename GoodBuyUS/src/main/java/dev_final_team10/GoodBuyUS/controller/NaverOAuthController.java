package dev_final_team10.GoodBuyUS.controller;

import dev_final_team10.GoodBuyUS.jwt.JwtService;
import dev_final_team10.GoodBuyUS.service.NaverOAuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class NaverOAuthController {

    private final NaverOAuthService naverOAuthService;
    private final JwtService jwtService;

    /**
     * 네이버 로그인 콜백 처리
     * 네이버에서 인증이 완료되면 code와 state를 전달받아 처리
     */
    @RequestMapping("/users/sociallogin")
    public void redirectToNaverLogin(HttpServletResponse response) throws IOException {
        String clientId = "WMviEjPZFWlKSGcKakso"; // 네이버 클라이언트 ID
        String redirectUri = "http://localhost:8080/naver/callback"; // 네이버 인증 완료 후 리다이렉트될 URI
        String state = "randomStateValue"; // 임의의 문자열 (CSRF 방지)

        // 인증 URL 생성
        String naverLoginUrl = "https://nid.naver.com/oauth2.0/authorize"
                + "?response_type=code"
                + "&client_id=" + clientId
                + "&redirect_uri=" + redirectUri
                + "&state=" + state;

        // 네이버 로그인 화면으로 리다이렉트
        response.sendRedirect(naverLoginUrl);
    }

    @RequestMapping("/naver/callback")
    public void naverCallback(
            @RequestParam String code,
            @RequestParam String state,
            HttpServletResponse response
    ) throws IOException {
        // 네이버 OAuth 서비스에서 토큰 처리
        Map<String, String> tokens = naverOAuthService.processNaverCallback(code, state);
        String accessToken = tokens.get("accessToken");
        String refreshToken = tokens.get("refreshToken");

        // AccessToken과 RefreshToken을 헤더에 추가
        naverOAuthService.sendAccessAndRefreshToken(response, accessToken, refreshToken);

        // 성공 페이지로 리다이렉트
        response.sendRedirect("/success?accessToken=" + accessToken);
    }




    /**
     * 사용자 정보 확인용 엔드포인트 (Optional)
     * 네이버 AccessToken을 활용해 사용자 정보를 가져옵니다.
     */
    @GetMapping("/naver/user-info")
    public ResponseEntity<Map<String, Object>> getUserInfo(@RequestParam String accessToken) {
        Map<String, Object> userInfo = naverOAuthService.getUserInfoFromAccessToken(accessToken);
        return ResponseEntity.ok(userInfo);
    }

//테스트용 화면
    @GetMapping("/success")
    public ResponseEntity<String> success(@RequestParam String accessToken) {
        // 네이버 Access Token 유효성 검증
        if (naverOAuthService.verifyNaverAccessToken(accessToken)) {
            Map<String, Object> userInfo = naverOAuthService.getUserInfoFromAccessToken(accessToken);
            String email = (String) userInfo.get("email");
            return ResponseEntity.ok("인증 성공: " + (email != null ? email : "Unknown"));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("인증 실패");
    }




}
