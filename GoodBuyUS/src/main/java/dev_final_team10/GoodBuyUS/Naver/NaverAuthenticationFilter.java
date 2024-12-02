package dev_final_team10.GoodBuyUS.jwt;

import dev_final_team10.GoodBuyUS.Naver.NaverLoginSuccessHandler;
import dev_final_team10.GoodBuyUS.domain.user.entity.Role;
import dev_final_team10.GoodBuyUS.domain.user.entity.User;
import dev_final_team10.GoodBuyUS.repository.UserRepository;
import dev_final_team10.GoodBuyUS.service.NaverOAuthService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
public class NaverAuthenticationFilter extends OncePerRequestFilter {

    private final NaverOAuthService naverOAuthService;
    private final UserRepository userRepository;
    private final NaverLoginSuccessHandler successHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String accessToken = extractAccessTokenFromHeader(request);

        if (accessToken != null && naverOAuthService.verifyNaverAccessToken(accessToken)) {
            Map<String, Object> userInfo = naverOAuthService.getUserInfoFromAccessToken(accessToken);

            String snsId = (String) userInfo.get("id");
            String snsType = "NAVER";

            // 기존 사용자 조회 또는 새 사용자 등록
            User user = userRepository.findBySnsTypeAndSnsId(snsType, snsId)
                    .orElseGet(() -> registerNewNaverUser(userInfo, snsType, snsId));

            // 인증 처리
            saveAuthentication(user);

            // 성공 핸들러 호출
            successHandler.onAuthenticationSuccess(request, response,
                    new UsernamePasswordAuthenticationToken(user, null, user.getRole().getAuthorities()));

            return; // 성공 후 필터 체인 종료
        }

        filterChain.doFilter(request, response); // 다음 필터로 전달
    }

    private String extractAccessTokenFromHeader(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7); // "Bearer " 이후의 값 추출
        }
        return null;
    }

    private User registerNewNaverUser(Map<String, Object> userInfo, String snsType, String snsId) {
        User newUser = User.builder()
                .email((String) userInfo.get("email"))
                .name((String) userInfo.get("name"))
                .nickname((String) userInfo.get("nickname"))
                .phone((String) userInfo.get("mobile"))
                .profile((String) userInfo.get("profile_image"))
                .snsType(snsType) // 네이버 로그인 시 SNS 타입 저장
                .snsId(snsId) // 네이버 ID 저장
                .role(Role.USER) // 기본적으로 USER 권한 설정
                .build();

        userRepository.save(newUser);

        log.info("새로운 네이버 사용자 등록: {}", newUser);
        return newUser;
    }

    private void saveAuthentication(User user) {
        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password("") // 비밀번호는 필요하지 않음
                .authorities(new SimpleGrantedAuthority(user.getRole().name()))
                .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("네이버 사용자 인증 성공: {}", user.getEmail());
    }
}
