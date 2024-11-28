package dev_final_team10.GoodBuyUS.Neighborhood;

import dev_final_team10.GoodBuyUS.domain.user.dto.UserSignUpDto;
import dev_final_team10.GoodBuyUS.domain.user.entity.User;
import dev_final_team10.GoodBuyUS.repository.UserRepository;
import dev_final_team10.GoodBuyUS.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Service
@Transactional
@ActiveProfiles("test")
@DisplayName("회원 테이블에 동네코드가 알맞게 들어가는지 확인")
public class NeighborhoodUser {
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;

    @Test
    public void neighborhood() throws Exception {
        UserSignUpDto userSignUpDto = new UserSignUpDto("test@naver.com", "0000", "test", "010451", "tesst", "서울 동작구 oo동 oo로 45");
        MultipartFile profile = new MockMultipartFile(
                "profile",                // 파라미터 이름
                "test.png",               // 파일명
                "image/png",              // MIME 타입
                "dummy file content".getBytes()  // 파일 내용
        );
        userService.signUp(userSignUpDto, profile);
        //회원테이블에 동네코드에 11590이 들어가야 테스트가 올바르게된 것

        Optional<User> savedUserOptional = userRepository.findByEmail("test@naver.com"); // 이메일을 통해 사용자 조회
        assertTrue(savedUserOptional.isPresent(), "사용자가 DB에 저장되지 않았습니다."); // Optional이 비어 있지 않음을 확인

        User savedUser = savedUserOptional.get(); // Optional에서 값을 안전하게 꺼냄
        savedUser.getNeighborhood();
        assertEquals(11590, savedUser.getNeighborhood().getNeighborhoodCode()); // 동네코드가 11590인지 확인

    }
}

