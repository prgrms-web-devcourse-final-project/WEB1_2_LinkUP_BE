package dev_final_team10.GoodBuyUS.service;

import dev_final_team10.GoodBuyUS.domain.user.entity.Neighborhood;
import dev_final_team10.GoodBuyUS.domain.user.dto.UserSignUpDto;
import dev_final_team10.GoodBuyUS.domain.user.entity.Role;
import dev_final_team10.GoodBuyUS.domain.user.entity.User;
import dev_final_team10.GoodBuyUS.jwt.JwtService;
import dev_final_team10.GoodBuyUS.repository.NeighborhoodRepository;
import dev_final_team10.GoodBuyUS.repository.UserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;


@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final NeighborhoodRepository neighborhoodRepository;
    private final JwtService jwtService;
    private final EmailService emailService;

    @Value("${file.upload-dir}")
    private String uploadDir;


    //자체 회원 가입 메소드(이름, 이메일, 비밀번호 전화번호)
    public ResponseEntity<?> signUpCheckEmail(UserSignUpDto userSignUpDto) throws Exception {

        if(userRepository.findByEmail(userSignUpDto.getEmail()).isPresent()){
            return new ResponseEntity<>(Map.of("error", "이미 존재하는 이메일입니다."), HttpStatus.BAD_REQUEST);
        }
        if(userRepository.findByPhone(userSignUpDto.getPhone()).isPresent()){
            return new ResponseEntity<>(Map.of("error", "이미 존재하는 전화번호입니다."), HttpStatus.BAD_REQUEST);
        }


//        user.passwordEncode(passwordEncoder);
//        userRepository.save(user);
//        userRepository.flush();

        return new ResponseEntity<>(Map.of("message", "이메일, 전화번호 중복 확인 완료"), HttpStatus.CREATED);
    }

//    public ResponseEntity<?> signUpCheckAddress(String address) {
//        //1.사용자 주소에서 지역명(시도군) 추출
//        String neighborhoodName = extractNeighborhoodName(address);
//
//        //2. 지역명 토대로 해당 행 추출
//        Neighborhood neighborhood = neighborhoodRepository.findByNeighborhoodName(neighborhoodName);
//
//
//    }





    // 프로필 이미지를 서버에 저장하는 메소드
    private String saveProfileImage(MultipartFile profile) throws IOException {
        if (profile == null || profile.isEmpty()) {
            throw new IOException("프로필 이미지를 선택해 주세요.");
        }

        // 파일 이름 추출
        String fileName = StringUtils.cleanPath(profile.getOriginalFilename());

        // 파일 저장 경로 설정
        Path targetLocation = Paths.get(uploadDir).resolve(fileName);

        // 디렉터리 생성 (경로가 없으면 생성)
        Files.createDirectories(targetLocation.getParent());

        // 파일 저장
        profile.transferTo(targetLocation);

        // 저장된 이미지 파일 경로 반환 (URL로 변경 가능)
        return targetLocation.toString();  // 또는 저장된 경로의 URL 반환 가능
    }

    //사용자 주소에서 지역명(시도군) 추출하는 메소드
    public String extractNeighborhoodName(String address){
        List<Neighborhood> neighborhoodList = neighborhoodRepository.findAll();

        String neighborhoodName = null;
        for(Neighborhood neighborhood: neighborhoodList){
            if(address.contains(neighborhood.getNeighborhoodName())){
                neighborhoodName = neighborhood.getNeighborhoodName();
                break;
            }
        }
        return neighborhoodName;
    }

    //비밀번호 찾기 - 사용자 이메일 인증 확인 후 비밀번호 재설정하는 메소드
    public ResponseEntity<?> findPassword(String email) throws MessagingException, IOException {
        User user = userRepository.findByEmail(email).orElse(null);

        //---가입되지 않은 이메일인 경우
        if(user == null){
            return ResponseEntity.badRequest().body(Map.of("error","가입되지 않은 이메일입니다."));
        }

        //---가입된 이메일인 경우
        //비밀번호 재설정 때 사용할 토큰 발급
        String token = jwtService.createAccessToken(email);
        //비밀번호 재설정 링크(이메일로 보내줄 링크)
        String resetLink = "http://localhost:8080/users/reset?token=" + token;
        //이메일 전송
        String htmlContent = "<html><body style='background-color: #ffffff !important; margin: 0 auto; max-width: 600px; word-break: break-all; padding-top: 50px; color: #000000;'>"
        + "<img class='logo' src='cid:logo'>"
        + "<h1 style='padding-top: 50px; font-size: 30px;'>이메일 주소 인증</h1>"
         + "<p style='padding-top: 20px; font-size: 18px; opacity: 0.6; line-height: 30px; font-weight: 400;'>안녕하세요? GoodBuyUs 입니다.<br />"
         + "하단의 버튼을 클릭하여, 비밀번호 재설정을 완료해주세요.<br />"
         + "감사합니다.</p>"
         + "<div class='code-box' style='margin-top: 50px; padding-top: 20px; color: #000000; padding-bottom: 20px; font-size: 25px; text-align: center; background-color: #f4f4f4; border-radius: 10px;'>"
         + "<a href='" + resetLink + "' style='text-decoration: none; color: #000000;'>비밀번호 재설정</a>"
         + "</div>"
         + "</body></html>";
        emailService.sendEmail(email, "비밀번호 재설정 링크", htmlContent);

        return ResponseEntity.ok(Map.of("message","비밀번호 재설정 링크가 이메일로 전송되었습니다."));
    }

    //비밀번호 찾기 했을 때 비밀번호 변경 메소드 (DB업데이트)
    public ResponseEntity<?> updatePassword(String token, String newPassword) {
        String email = jwtService.extractEmail(token).orElse(null);
        User user = userRepository.findByEmail(email).orElse(null);
        if (user != null) {
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
        }
        return ResponseEntity.ok(Map.of("message","비밀번호가 성공적으로 변경되었습니다."));
    }



}
