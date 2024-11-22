package dev_final_team10.GoodBuyUS.service;

import dev_final_team10.GoodBuyUS.domain.user.dto.UserSignUpDto;
import dev_final_team10.GoodBuyUS.domain.user.entity.Role;
import dev_final_team10.GoodBuyUS.domain.user.entity.User;
import dev_final_team10.GoodBuyUS.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    //자체 회원 가입 메소드
    public ResponseEntity<String> signUp(UserSignUpDto userSignUpDto) throws Exception {

        if(userRepository.findByEmail(userSignUpDto.getEmail()).isPresent()){
            throw new Exception("이미 존재하는 이메일입니다.");
        }
        if(userRepository.findByNickname(userSignUpDto.getNickname()).isPresent()){
            throw new Exception("이미 존재하는 닉네임입니다.");
        }
        if(userRepository.findByPhone(userSignUpDto.getPhone()).isPresent()){
            throw new Exception("이미 존재하는 전화번호입니다.");
        }

        //User Entity 생성 후 DB저장
        User user = User.builder()
                .email(userSignUpDto.getEmail())
                .password(userSignUpDto.getPassword())
                .name(userSignUpDto.getName())
                .phone(userSignUpDto.getPhone())
                .nickname(userSignUpDto.getNickname())
                .profile(userSignUpDto.getProfile())
                .role(Role.USER)
                .refreshToken(null)
                .build();

        user.passwordEncode(passwordEncoder);
        userRepository.save(user);
        userRepository.flush();

        return ResponseEntity.ok("회원가입 완료");
    }
}
