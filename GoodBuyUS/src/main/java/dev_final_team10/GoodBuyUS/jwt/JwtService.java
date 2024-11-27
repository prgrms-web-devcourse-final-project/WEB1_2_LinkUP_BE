package dev_final_team10.GoodBuyUS.jwt;

import com.auth0.jwt.algorithms.Algorithm;
import dev_final_team10.GoodBuyUS.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.auth0.jwt.JWT;
import java.util.Date;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Getter
@Log4j2
public class JwtService {

    //설정 파일의 프로퍼티 주입
    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.access.expiration}")
    private Long accessTokenExpiration;

    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenExpiration;

    @Value("${jwt.access.header}")
    private String accessHeader;

    @Value("${jwt.refresh.header}")
    private String refreshHeader;

    private final UserRepository userRepository;


    //AccessToken 생성 메소드
    public String createAccessToken(String email) {
        Date now = new Date();
        return JWT.create() // JWT 토큰을 생성하는 빌더 반환
                .withSubject("accessToken") // JWT의 Subject 지정
                .withExpiresAt(new Date(now.getTime() + accessTokenExpiration)) // 토큰 만료 시간 설정
                //클레임(토큰에 포함되는 정보)으로는 email 사용
                .withClaim("email", email)
                .sign(Algorithm.HMAC512(secretKey)); //application-jwt.yml에서 지정한 secret 키로 암호화
    }


    //RefreshToken 생성
    public String createRefreshToken() {
        Date now = new Date();
        return JWT.create()
                .withSubject("refreshToken")
                .withExpiresAt(new Date(now.getTime() + refreshTokenExpiration))
                .sign(Algorithm.HMAC512(secretKey));
    }


    //AccessToken 헤더에 실어서 보내기
    public void sendAccessToken(HttpServletResponse response, String accessToken) {
        response.setStatus(HttpServletResponse.SC_OK);

        response.setHeader(accessHeader, accessToken);
        log.info("재발급된 Access Token : {}", accessToken);
    }


    //AccessToken + RefreshToken 헤더에 실어서 보내기 - 로그인 시 발급
    public void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken) {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader(accessHeader, accessToken);
        response.setHeader(refreshHeader, refreshToken);
        log.info("Access Token, Refresh Token 헤더 설정 완료");
    }

    //헤더에서 토큰을 추출하는 공통 메서드
    private Optional<String> extractToken(HttpServletRequest request, String headerName) {
        return Optional.ofNullable(request.getHeader(headerName))
                .filter(refreshToken -> refreshToken.startsWith("Bearer "))    //"Bearer "로 시작하는지 확인
                .map(refreshToken -> refreshToken.replace("Bearer ", "")); //"Bearer " 제외하고 순수 토큰만 가져오기
    }


    //헤더에서 AccessToken 추출
    public Optional<String> extractAccessToken(HttpServletRequest request) {
        return extractToken(request, accessHeader);
    }

    //헤더에서 RefreshToken 추출
    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        return extractToken(request, refreshHeader);
    }


    //AccessToken에서 Email 추출 - 사용자 확인을 위해
    public Optional<String> extractEmail(String accessToken) {
        try {
            // 토큰 유효성 검사
            return Optional.ofNullable(JWT.require(Algorithm.HMAC512(secretKey))
                    .build()
                    .verify(accessToken) // accessToken을 검증하고 유효하지 않다면 예외 발생
                    .getClaim("email") // claim(Emial) 가져오기
                    .asString());
        } catch (Exception e) {
            log.error("액세스 토큰이 유효하지 않습니다.");
            return Optional.empty();
        }
    }


    //RefreshToken DB 저장(업데이트) - 로그인할 때 RefreshToken발급하고 DB에 저장
    public void updateRefreshToken(String email, String refreshToken) {
        userRepository.findByEmail(email)
                .ifPresentOrElse(
                        user -> user.updateRefreshToken(refreshToken),
                        () -> new Exception("일치하는 회원이 없습니다.")
                );
    }

    //토큰 유효성 검사
    public boolean isTokenValid(String token) {
        try {
            JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token);
            return true;
        } catch (Exception e) {
            log.error("유효하지 않은 토큰입니다. {}", e.getMessage());
            return false;
        }
    }
}
