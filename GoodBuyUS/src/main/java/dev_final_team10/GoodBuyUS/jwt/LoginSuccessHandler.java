package dev_final_team10.GoodBuyUS.jwt;



import com.fasterxml.jackson.databind.ObjectMapper;
import dev_final_team10.GoodBuyUS.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;
<<<<<<< HEAD
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
=======
import java.util.HashMap;
import java.util.Map;
>>>>>>> 35cc3a3fb06474bc92a2ba3bf806ed62c2182977

//로그인 성공 시
@Log4j2
@RequiredArgsConstructor
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Value("${jwt.access.expiration}")
    private String accessTokenExpiration;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) {
        String email = extractUsername(authentication); // 인증 정보에서 Username(email) 추출
        String accessToken = jwtService.createAccessToken(email); // JwtService의 createAccessToken을 사용하여 AccessToken 발급
        String refreshToken = jwtService.createRefreshToken(); // JwtService의 createRefreshToken을 사용하여 RefreshToken 발급


        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority) // 권한을 문자열로 변환
                .findFirst()
                .orElse("ROLE_USER"); // 만약 권한이 없으면 기본 ROLE_USER로 설정
        Map<String, Object> responseBody = new HashMap<>();
<<<<<<< HEAD

=======
>>>>>>> 35cc3a3fb06474bc92a2ba3bf806ed62c2182977
        responseBody.put("roles", role);  // roles는 사용자의 권한 목록
        try {
            response.setContentType("application/json");
            response.getWriter().write(new ObjectMapper().writeValueAsString(responseBody));
        } catch (
                IOException e) {
            log.error("응답 작성 중 오류 발생", e);
        }

<<<<<<< HEAD



=======
>>>>>>> 35cc3a3fb06474bc92a2ba3bf806ed62c2182977
        jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken); // 응답 헤더에 AccessToken, RefreshToken 실어서 응답



        userRepository.findByEmail(email)
                .ifPresent(user -> {
                    user.updateRefreshToken(refreshToken);
                    userRepository.saveAndFlush(user);
                });
        log.info("로그인에 성공하였습니다. 이메일 : {}", email);
        log.info("로그인에 성공하였습니다. AccessToken : {}", accessToken);
        log.info("발급된 AccessToken 만료 기간 : {}", accessTokenExpiration);
    }


    private String extractUsername(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUsername();
    }
}