package dev_final_team10.GoodBuyUS.jwt;

import dev_final_team10.GoodBuyUS.domain.user.entity.User;
import dev_final_team10.GoodBuyUS.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;



import java.io.IOException;

/*
    Jwt 인증 필터
    "/users/login" 이외의 요청이 왔을 때 처리하는 필터

    기본적으로 사용자는 요청 헤더에 AccessToken만 담아서 요청
    AccessToken 만료 시에만 요청 헤더에 RefreshToken 실어서 AccessToken재발급 요청(RefreshToken도 같이 재발급해줌)


    1. 요청 헤더에 RefreshToken이 있는 경우 -> DB의 RefreshToken과 비교하여 일치하면 AccessToken, RefreshToken 재발급
                                            인증 성공 처리는 하지 않고 실패 처리
    2. 요청 헤더에 RefreshToken 없는 경우
        2-1. AccessToken이 유효한 경우 -> 인증 성공 처리, RefreshToken 재발급 X
        2-2. AccessToken이 없거나 유효하지 않은 경우 -> 인증 실패 처리, 403 ERROR

 */


@RequiredArgsConstructor
@Log4j2
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
<<<<<<< HEAD
=======

>>>>>>> 35cc3a3fb06474bc92a2ba3bf806ed62c2182977
        //OPTIONS 요청에 대해 필터를 처리하지 않음 (CORS 프리플라이트 요청)
        if (request.getMethod().equals(HttpMethod.OPTIONS.name())) {
            filterChain.doFilter(request, response);
            return;  // OPTIONS 요청은 JWT 필터를 거치지 않도록 한다.
        }

        //로그인 요청의 경우는 Filter 작동 X
        if (request.getRequestURI().equals("/users/login")) {
            filterChain.doFilter(request, response); // 로그인 요청이 들어오면, 다음 필터 호출
            return; // return으로 이후 필터 진행 막기
        }

        // 사용자 요청 헤더에서 RefreshToken이 있는지 확인
        // -> RefreshToken이 없거나 유효하지 않다면(DB에 저장된 RefreshToken과 다르다면) null을 반환
        String refreshToken = jwtService.extractRefreshToken(request)
                .filter(jwtService::isTokenValid)
                .orElse(null);

        // 1. RefreshToken이 요청 헤더에 존재하는 경우 - 사용자의 AccessToken 재발급 요청
        // -> RefreshToken이 DB의 RefreshToken과 일치하는지 판단 후,일치한다면 AccessToken과 RefreshToken재발급
        if (refreshToken != null) {
            checkRefreshTokenAndReIssueAccessToken(response, refreshToken);
            return; //return으로 필터 진행 막기
        }

        // 2. RefreshToken이 요청 헤더에 없는 경우 - AccessToken을 검사하고 인증
        // AccessToken이 없거나 유효하지 않다면, 인증 객체가 담기지 않은 상태로 다음 필터로 넘어가기 때문에 403 에러 발생
        // AccessToken이 유효하다면, 인증 객체가 담긴 상태로 다음 필터로 넘어가기 때문에 인증 성공
        if (refreshToken == null) {
            checkAccessTokenAndAuthentication(request, response, filterChain);
        }




    }

    //RefreshToken으로 유저 정보 찾기 & AccessToken/RefreshToken 재발급 메소드
    public void checkRefreshTokenAndReIssueAccessToken(HttpServletResponse response, String refreshToken) {
        userRepository.findByRefreshToken(refreshToken)
                .ifPresent(user -> {
                    String reIssuedRefreshToken = reIssueRefreshToken(user);
                    //응답헤더로 Token보내기
                    jwtService.sendAccessAndRefreshToken(response, jwtService.createAccessToken(user.getEmail()),
                            reIssuedRefreshToken);
                });
    }

    //RefreshToken 재발급 & DB에 리프레시 토큰 업데이트 메소드
    private String reIssueRefreshToken(User user) {
        String reIssuedRefreshToken = jwtService.createRefreshToken();
        user.updateRefreshToken(reIssuedRefreshToken);
        userRepository.saveAndFlush(user);
        return reIssuedRefreshToken;
    }

    //액세스 토큰 체크 & 인증 처리 메소드
    public void checkAccessTokenAndAuthentication(HttpServletRequest request, HttpServletResponse response,
                                                  FilterChain filterChain) throws ServletException, IOException {
        jwtService.extractAccessToken(request)
                .filter(jwtService::isTokenValid)
                .ifPresent(accessToken -> jwtService.extractEmail(accessToken)
                        .ifPresent(email -> userRepository.findByEmail(email)
                                .ifPresent(this::saveAuthentication)));

        filterChain.doFilter(request, response);
    }

    //인증 허가 메소드
    /*
     * 파라미터의 유저 : 우리가 만든 회원 객체 / 빌더의 유저 : UserDetails의 User 객체
     *
     * new UsernamePasswordAuthenticationToken()로 인증 객체인 Authentication 객체 생성
     * UsernamePasswordAuthenticationToken의 파라미터
     * 1. 위에서 만든 UserDetailsUser 객체 (유저 정보)
     * 2. credential(보통 비밀번호로, 인증 시에는 보통 null로 제거)
     * 3. Collection < ? extends GrantedAuthority>로,
     * UserDetails의 User 객체 안에 Set<GrantedAuthority> authorities이 있어서 getter로 호출한 후에,
     * new NullAuthoritiesMapper()로 GrantedAuthoritiesMapper 객체를 생성하고 mapAuthorities()에 담기
     *
     * SecurityContextHolder.getContext()로 SecurityContext를 꺼낸 후,
     * setAuthentication()을 이용하여 위에서 만든 Authentication 객체에 대한 인증 허가 처리
     */
    public void saveAuthentication(User myUser) {
        String password = myUser.getPassword();
//        if (password == null) { // 소셜 로그인 유저의 비밀번호 임의로 설정 하여 소셜 로그인 유저도 인증 되도록 설정
//            password = PasswordUtil.generateRandomPassword();
//        }

        UserDetails userDetailsUser = org.springframework.security.core.userdetails.User.builder()
                .username(myUser.getEmail())
                .password(password)
                .roles(myUser.getRole().name())
                .build();

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(userDetailsUser, null,
                        authoritiesMapper.mapAuthorities(userDetailsUser.getAuthorities()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}