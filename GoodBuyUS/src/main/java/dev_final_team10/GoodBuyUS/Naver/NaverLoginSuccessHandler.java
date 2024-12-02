package dev_final_team10.GoodBuyUS.Naver;

import dev_final_team10.GoodBuyUS.domain.user.entity.User;
import dev_final_team10.GoodBuyUS.jwt.JwtService;
import dev_final_team10.GoodBuyUS.repository.UserRepository;
import dev_final_team10.GoodBuyUS.service.NaverOAuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

@Slf4j
@RequiredArgsConstructor
public class NaverLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final NaverOAuthService naverOAuthService; // JwtService -> NaverOAuthService로 변경
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) {
        // 기존 로직과 동일하게 처리
        String snsId = extractSnsId(authentication);
        String snsType = "NAVER";

        userRepository.findBySnsTypeAndSnsId(snsType, snsId).ifPresentOrElse(user -> {
            String accessToken = request.getHeader("Authorization").substring(7);
            String refreshToken = request.getHeader("Refresh-Token");

            if (refreshToken == null || refreshToken.isEmpty()) {
                log.warn("리프레시 토큰이 누락되었습니다. SNS ID: {}", snsId);
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            // 클라이언트에 네이버 제공 토큰 전송
            response.setHeader("Authorization", "Bearer " + accessToken);
            response.setHeader("Refresh-Token", "Bearer " + refreshToken);

            // 리프레시 토큰 업데이트
            user.updateRefreshToken(refreshToken);
            userRepository.saveAndFlush(user);

            log.info("네이버 로그인 성공. SNS ID: {}", snsId);
            log.info("Access Token: {}", accessToken);
            log.info("Refresh Token: {}", refreshToken);

        }, () -> {
            log.warn("네이버 로그인 실패. SNS 사용자 정보를 찾을 수 없음. SNS ID: {}", snsId);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        });
    }

    private String extractSnsId(Authentication authentication) {
        return authentication.getName(); // 인증 객체에서 SNS ID 추출
    }
}

