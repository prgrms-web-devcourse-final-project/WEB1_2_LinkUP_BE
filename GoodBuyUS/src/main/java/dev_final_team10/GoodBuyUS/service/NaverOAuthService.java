package dev_final_team10.GoodBuyUS.service;

import dev_final_team10.GoodBuyUS.domain.user.entity.Role;
import dev_final_team10.GoodBuyUS.domain.user.entity.User;
import dev_final_team10.GoodBuyUS.jwt.JwtService;
import dev_final_team10.GoodBuyUS.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class NaverOAuthService {

    private final RestTemplate restTemplate;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.naver.redirect-uri}")
    private String redirectUri;

    /**
     * 네이버 로그인 프로세스
     */
    public void processNaverLogin(String code, String state, HttpServletResponse response) {
        // Step 1: 네이버 Access Token 요청
        Map<String, Object> tokenResponse = requestNaverAccessToken(code, state);
        String accessToken = (String) tokenResponse.get("access_token");

        // Step 2: 네이버 사용자 정보 요청
        Map<String, Object> userInfo = getUserInfoFromAccessToken(accessToken);

        // Step 3: 사용자 저장/업데이트 및 JWT 발급
        User user = saveOrUpdateUser(userInfo);
        issueJwtTokens(user, response);
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
    private Map<String, Object> getUserInfoFromAccessToken(String accessToken) {
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
     * 사용자 저장 또는 업데이트
     */
    private User saveOrUpdateUser(Map<String, Object> userInfo) {
        String snsId = (String) userInfo.get("id");
        String email = (String) userInfo.get("email");
        String name = (String) userInfo.get("name");
        String phone = (String) userInfo.get("mobile");
        String profileImage = (String) userInfo.get("profile_image");

        // 이메일 중복 검증
        Optional<User> existingUser = userRepository.findByEmail(email);

        if (existingUser.isPresent()) {
            User user = existingUser.get();

            // 자체 로그인 계정인 경우 예외 처리
            if (!"NAVER".equals(user.getSnsType())) {
                throw new IllegalStateException("이미 존재하는 이메일입니다: " + email);
            }

            // 기존 유저 반환 (업데이트 없이)
            return user;
        }

        // 신규 유저 생성
        String defaultPassword = passwordEncoder.encode("socialDummyPassword");

        User newUser = User.builder()
                .email(email)
                .password(defaultPassword)
                .name(name)
                .phone(phone)
                .profile(profileImage)
                .snsType("NAVER")
                .snsId(snsId)
                .role(Role.USER)
                .build();

        return userRepository.save(newUser);
    }


    /**
     * 자체 JWT 발급
     */
    private void issueJwtTokens(User user, HttpServletResponse response) {
        // Step 1: JWT 생성
        String accessToken = jwtService.createAccessToken(user.getEmail());
        String refreshToken = jwtService.createRefreshToken();

        // Step 2: Refresh Token 저장
        user.updateRefreshToken(refreshToken);
        userRepository.save(user);

        // Step 3: 리다이렉트 URL 생성
        String redirectUrl = "http://15.164.5.135/signin"
                + "?accessToken=" + accessToken
                + "&refreshToken=" + refreshToken;

        try {
            // Step 4: 클라이언트를 리다이렉트
            response.sendRedirect(redirectUrl);
            log.info("클라이언트를 {}로 리다이렉트", redirectUrl);
        } catch (IOException e) {
            log.error("리다이렉트 중 오류 발생", e);
        }
    }


    public String generateNaverLoginUrl() {
        return UriComponentsBuilder.fromHttpUrl("https://nid.naver.com/oauth2.0/authorize")
                .queryParam("response_type", "code")
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("state", "randomStateValue")
                .toUriString();
    }
}
