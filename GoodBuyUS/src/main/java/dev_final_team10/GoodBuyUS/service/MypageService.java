package dev_final_team10.GoodBuyUS.service;

import dev_final_team10.GoodBuyUS.domain.user.entity.Neighborhood;
import dev_final_team10.GoodBuyUS.domain.user.entity.User;
import dev_final_team10.GoodBuyUS.repository.NeighborhoodRepository;
import dev_final_team10.GoodBuyUS.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class MypageService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final NeighborhoodRepository neighborhoodRepository;

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

    //동네 변경하는 메소드
    public ResponseEntity<?> chageNeighbor(String newAddress) {
        //새로 입력한 주소로 동네테이블에서 해당하는 행 찾기
        String neighborhoodName =  userService.extractNeighborhoodName(newAddress);
        Neighborhood neighborhood = neighborhoodRepository.findByNeighborhoodName(neighborhoodName);

        //현재 로그인한 사용자 정보 가져오기
        String email = getCurrentUserEmail();
        User user = userRepository.findByEmail(email).orElse(null);

        user.setAddress(newAddress);
        user.setNeighborhood(neighborhood);
        userRepository.save(user);

        return ResponseEntity.ok(Map.of("message","주소가 성공적으로 변경되었습니다."));
    }
}
