package dev_final_team10.GoodBuyUS.service;

import dev_final_team10.GoodBuyUS.domain.user.User;
import dev_final_team10.GoodBuyUS.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MypageService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    //현재 로그인한 사용자의 이메일을 가져오는 메소드
    public String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    //현재 비밀번호 확인하는 메소드
    public boolean isCurrentPasswordValid(String userEmail, String currentPassword) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(()->
                new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        boolean matches = passwordEncoder.matches(currentPassword, user.getPassword());
        System.out.println(matches);
        return passwordEncoder.matches(currentPassword, user.getPassword());
    }

    //비밀번호 변경 했을 때 비밀번호 업데이트하는 메소드
    public void updateNewPassword(String userEmail, String newPassword) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

}
