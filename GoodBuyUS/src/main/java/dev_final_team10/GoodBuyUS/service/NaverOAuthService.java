package dev_final_team10.GoodBuyUS.service;

import dev_final_team10.GoodBuyUS.domain.user.entity.Role;
import dev_final_team10.GoodBuyUS.domain.user.entity.User;
import dev_final_team10.GoodBuyUS.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class NaverOAuthService {

    private final RestTemplate restTemplate;
    private final UserRepository userRepository;

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.naver.redirect-uri}")
    private String redirectUri;

    @Value("${jwt.access.header}")
    private String accessHeader;

    @Value("${jwt.refresh.header}")
    private String refreshHeader;
     /* 네이버 Access Token 유효성 검증
     */
    public boolean verifyNaverAccessToken(String accessToken) {
        String validationUrl = "https://openapi.naver.com/v1/nid/me";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(validationUrl, HttpMethod.GET, entity, Map.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            log.error("네이버 Access Token 검증 실패: {}", e.getMessage());
            return false; // 유효하지 않은 경우
        }
    }

    /**
     * 네이버 OAuth 콜백 처리: Access Token 및 Refresh Token 발급 후 사용자 저장/업데이트
     */
    public Map<String, String> processNaverCallback(String code, String state) {
        // Step 1: 네이버 Access Token 요청
        Map<String, Object> tokenResponse = requestNaverAccessToken(code, state);
        String accessToken = (String) tokenResponse.get("access_token");
        String refreshToken = (String) tokenResponse.get("refresh_token");

        // Step 2: 네이버 사용자 정보 요청
        Map<String, Object> userInfo = getUserInfoFromAccessToken(accessToken);

        // Step 3: 사용자 저장 또는 업데이트
        saveOrUpdateUser(userInfo, refreshToken);

        return Map.of("accessToken", accessToken, "refreshToken", refreshToken);
    }

    /**
     * 네이버 Access Token 요청
     */
    private Map<String, Object> requestNaverAccessToken(String code, String state) {
        String tokenUri = "https://nid.naver.com/oauth2.0/token";

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(tokenUri)
                .queryParam("grant_type", "authorization_code")
                .queryParam("client_id", clientId)
                .queryParam("client_secret", clientSecret)
                .queryParam("code", code)
                .queryParam("state", state);

        ResponseEntity<Map> response = restTemplate.getForEntity(uriBuilder.toUriString(), Map.class);

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new RuntimeException("네이버 Access Token 요청 실패");
        }

        return response.getBody();
    }

    /**
     * 네이버 사용자 정보 요청
     */
    public Map<String, Object> getUserInfoFromAccessToken(String accessToken) {
        String userInfoUri = "https://openapi.naver.com/v1/nid/me";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(userInfoUri, HttpMethod.GET, entity, Map.class);

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new RuntimeException("네이버 사용자 정보 요청 실패");
        }

        return (Map<String, Object>) response.getBody().get("response");
    }

    /**
     * 네이버 사용자 정보 저장 또는 업데이트
     */
    private void saveOrUpdateUser(Map<String, Object> userInfo, String refreshToken) {
        String email = (String) userInfo.get("email");
        String snsId = (String) userInfo.get("id");
        String name = (String) userInfo.get("name");
        String nickname = (String) userInfo.get("nickname");
        String profile = (String) userInfo.get("profile_image");
        String phone = (String) userInfo.get("mobile");

        User user = userRepository.findBySnsTypeAndSnsId("NAVER", snsId)
                .orElseGet(() -> User.builder()
                        .email(email)
                        .name(name)
                        .nickname(nickname != null ? nickname : name)
                        .phone(phone)
                        .profile(profile)
                        .snsType("NAVER")
                        .snsId(snsId)
                        .role(Role.USER)
                        .warnings(0) // 경고 횟수를 명시적으로 0으로 설정
                        .build());

        user.updateRefreshToken(refreshToken);
        userRepository.save(user);
    }
    public void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken) {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader(accessHeader, "Bearer " + accessToken);
        response.setHeader(refreshHeader, "Bearer " + refreshToken);
        log.info("네이버 Access Token과 Refresh Token 헤더 설정 완료: {}, {}", accessToken, refreshToken);
    }
    public Map<String, Object> refreshNaverAccessToken(String refreshToken) {
        String tokenUri = "https://nid.naver.com/oauth2.0/token";
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(tokenUri)
                .queryParam("grant_type", "refresh_token")
                .queryParam("refresh_token", refreshToken)
                .queryParam("client_id", clientId)
                .queryParam("client_secret", clientSecret);

        ResponseEntity<Map> response = restTemplate.getForEntity(uriBuilder.toUriString(), Map.class);

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new RuntimeException("네이버 Refresh Token 요청 실패");
        }
        return response.getBody();
    }

}
