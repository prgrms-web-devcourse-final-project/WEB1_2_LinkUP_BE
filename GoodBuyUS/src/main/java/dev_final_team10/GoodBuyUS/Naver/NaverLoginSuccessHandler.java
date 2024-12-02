package dev_final_team10.GoodBuyUS.Naver;

import dev_final_team10.GoodBuyUS.domain.user.entity.User;
import dev_final_team10.GoodBuyUS.jwt.JwtService;
import dev_final_team10.GoodBuyUS.repository.UserRepository;
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

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Value("${jwt.access.expiration}")
    private String accessTokenExpiration;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) {
        String snsId = extractSnsId(authentication); // 인증 정보에서 SNS ID 추출
        String snsType = "NAVER"; // 네이버 인증

        userRepository.findBySnsTypeAndSnsId(snsType, snsId).ifPresentOrElse(user -> {
            String accessToken = jwtService.createAccessToken(user.getEmail());
            String refreshToken = jwtService.createRefreshToken();

            // 토큰을 클라이언트로 전송
            jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);

            // RefreshToken 업데이트
            user.updateRefreshToken(refreshToken);
            userRepository.saveAndFlush(user);

            log.info("네이버 로그인 성공. SNS ID: {}", snsId);
            log.info("발급된 AccessToken: {}", accessToken);
            log.info("발급된 RefreshToken: {}", refreshToken);

        }, () -> {
            log.warn("네이버 로그인 실패. SNS 사용자 정보를 찾을 수 없음. SNS ID: {}", snsId);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        });
    }

    /**
     * 인증 객체에서 SNS ID 추출
     */
    private String extractSnsId(Authentication authentication) {
        // 인증 정보에서 SNS ID 추출 (NaverAuthenticationFilter와 연동 필요)
        return authentication.getName(); // 필요 시 추가 정보 사용
    }
}
