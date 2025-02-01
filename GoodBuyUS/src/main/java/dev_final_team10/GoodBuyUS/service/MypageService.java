package dev_final_team10.GoodBuyUS.service;


import dev_final_team10.GoodBuyUS.domain.community.dto.PostResponseDto;
import dev_final_team10.GoodBuyUS.domain.community.dto.WriteModifyPostDto;
import dev_final_team10.GoodBuyUS.domain.community.entity.*;
import dev_final_team10.GoodBuyUS.domain.order.dto.OrdersDTO;
import dev_final_team10.GoodBuyUS.domain.order.dto.RefundListDto;
import dev_final_team10.GoodBuyUS.domain.order.entity.Order;
import dev_final_team10.GoodBuyUS.domain.order.entity.OrderStatus;
import dev_final_team10.GoodBuyUS.domain.payment.entity.MainPayment;
import dev_final_team10.GoodBuyUS.domain.user.dto.MypageDefaultDto;
import dev_final_team10.GoodBuyUS.domain.user.entity.Neighborhood;
import dev_final_team10.GoodBuyUS.domain.user.entity.User;
import dev_final_team10.GoodBuyUS.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Log4j2
@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class MypageService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final NeighborhoodRepository neighborhoodRepository;
    private final CommunityPostRepository communityPostRepository;
    private final MainPaymentRepository mainPaymentRepository;
    private final OrderRepository orderRepository;
    private final ParticipationsRepository participationsRepository;

  @Value("${file.upload-dir}")
    private String uploadDir;
    //현재 로그인한 사용자의 이메일을 가져오는 메소드
    public String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    //현재 비밀번호 확인하는 메소드
    public boolean isCurrentPasswordValid(String userEmail, String currentPassword) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(() ->
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
        String neighborhoodName = userService.extractNeighborhoodName(newAddress);
        Neighborhood neighborhood = neighborhoodRepository.findByNeighborhoodName(neighborhoodName);

        //현재 로그인한 사용자 정보 가져오기
        String email = getCurrentUserEmail();
        User user = userRepository.findByEmail(email).orElse(null);

        user.setAddress(newAddress);
        user.setNeighborhood(neighborhood);
        userRepository.save(user);

        return ResponseEntity.ok(Map.of("message", "주소가 성공적으로 변경되었습니다."));
    }

    //커뮤니티에 작성한 글 수정하는 메소드
    public PostResponseDto modifyPost(WriteModifyPostDto writeModifyPostDto, List<MultipartFile> images, Long id) throws IOException {
        CommunityPost communityPost = communityPostRepository.findById(id).orElse(null);
        //현재 사용자 정보 가져오기(글 작성자)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName()).orElse(null);
        //현재 사용자 동네 정보 가져오기
        Neighborhood neighborhood = user.getNeighborhood();
        //카테고리 설정
        CommunityCategory communityCategory = writeModifyPostDto.getCategory();

        communityPost.updateFields(writeModifyPostDto, user, neighborhood, communityCategory);
        List<String> postImages =  new ArrayList<String>();
        for(MultipartFile image: images){
            String save = saveImage(image);
            postImages.add(save);

        }
        if(communityPost.getStatus() == postStatus.REJECTED){
            communityPost.setStatus(postStatus.NOT_APPROVED);
        }
        communityPost = writeModifyPostDto.toEntityForCreate(user,neighborhood,postImages);
        //DB 저장
        communityPostRepository.save(communityPost);

        return PostResponseDto.of(communityPost);

    }

    //이미지를 서버에 저장하는 메소드
    private String saveImage(MultipartFile profile) throws IOException {
        if (profile == null || profile.isEmpty()) {
            throw new IOException("이미지를 넣어주세요.");
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


    //내가 쓴 글 목록 보기
    public List<PostResponseDto> myPostList() {
        //현재 사용자 정보 가져오기
        User user = userRepository.findByEmail(getCurrentUserEmail()).orElse(null);

        //현재 사용자 글 목록 가져오기
        List<CommunityPost> communityPosts = communityPostRepository.findAllByUserIdAndStatusNot(user.getId(), postStatus.DELETED );

        List<PostResponseDto> postResponseDtos = new ArrayList<>();

        for (CommunityPost communityPost : communityPosts) {
            postResponseDtos.add(PostResponseDto.of(communityPost));
        }
        return postResponseDtos;
    }

    // 주문 내역 확인하기
    public List<OrdersDTO> orderlist(String userEmail) {
        List<Order> orders = orderRepository.findOrderByUser(userRepository.findByEmail(userEmail).orElseThrow(() -> new NoSuchElementException("없는 유저")));
        List<OrdersDTO> ordersDTOS = new ArrayList<>();
        for (Order order : orders) {
            MainPayment payment = mainPaymentRepository.findByOrder(order).orElseThrow(null);
            OrdersDTO ordersDTO = OrdersDTO.of(order.getOrderName(), order.getPrice(), order.getCreatedAt(), payment.getPaymentStatus(),
                    payment.getPaymentKey(), order.getQuantity(), order.getDelivery(), order.getProductPost().getPostId(), order.getProductPost().getPostURL());
            ordersDTOS.add(ordersDTO);
        }
        return ordersDTOS;
    }

    // 마이 페이지 접근
    public ResponseEntity<?> mypageMain() {
        try {
            User user = userRepository.findByEmail(getCurrentUserEmail()).orElseThrow(() -> new NoSuchElementException("없는 회원입니다"));
            MypageDefaultDto mypageDefaultDto = new MypageDefaultDto();
            mypageDefaultDto.setAddress(user.getAddress());
            mypageDefaultDto.setName(user.getName());
            mypageDefaultDto.setNickname(user.getNickname());
            mypageDefaultDto.setProfile(user.getProfile());
            mypageDefaultDto.setPhoneNum(user.getPhone());
            return ResponseEntity.ok(mypageDefaultDto);
        } catch (NoSuchElementException e) {
            log.error("없는 유저의 마이페이지 접근 줄게 없음 {}", e.getMessage());
            return new ResponseEntity<>("없는 회원입니다" + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // 유저 프로필 변경
    public ResponseEntity<?> editProfile(MultipartFile multipartFile) throws Exception{
        try {
            User user = userRepository.findByEmail(getCurrentUserEmail()).orElseThrow(() -> new NoSuchElementException("없는 회원입니다"));
            String updateProfile = saveProfileImage(multipartFile);
            user.changeProfile(updateProfile);
            return new ResponseEntity<>("프로필 변경 완료",HttpStatus.OK);
        } catch (NoSuchElementException e){
            log.error("없는 회원입니다");
            return new ResponseEntity<>("없는 회원입니다" , HttpStatus.NOT_FOUND);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

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

      //나의 공구 참여 내역 목록
    public List<PostResponseDto> communityJoinList() {
        //현재 로그인한 사용자 정보
        User user = userRepository.findByEmail(getCurrentUserEmail()).orElse(null);
        //나의 참여 내역 가져오기
        List<participationStatus> statuses = List.of(
                participationStatus.JOIN,
                participationStatus.PAYMENT_STANDBY,
                participationStatus.PAYMENT_COMPLETE
        );
        List<Participations> participationsList = participationsRepository.findAllByUserIdAndStatusIn(user.getId(),statuses);
        log.info("ParList" + participationsList);
        List<CommunityPost> communityPosts = new ArrayList<>();
        for(Participations participations : participationsList){
            communityPosts.add(communityPostRepository.findByCommunityPostId(participations.getCommunityPost().getCommunityPostId()));
        }
        log.info("CommunityPosts" + communityPosts);
        List<PostResponseDto> postResponseDtos = new ArrayList<>();
        for(CommunityPost communityPost : communityPosts){
            postResponseDtos.add(PostResponseDto.of(communityPost));
        }
        log.info("PostResponseDtos" + postResponseDtos);
        return postResponseDtos;
    }

    public ResponseEntity<?> refundlist() {
        String email = getCurrentUserEmail();
        try{
            User user = userRepository.findByEmail(email).orElseThrow(()-> new NoSuchElementException("없는 회원입니다"));
            List<Order> orders = user.getOrders().stream()
                    .filter(order -> order.getOrderStatus().equals(OrderStatus.CANCEL))
                    .toList();
            List<RefundListDto> refundLists = new ArrayList<>();
            for (Order order : orders) {
                MainPayment payment = mainPaymentRepository.findByOrder(order).orElseThrow(()-> new NoSuchElementException("잘못된 주문입니다"));
                RefundListDto refundListDto = new RefundListDto(order.getProductPost().getTitle(),payment.getQuantity(),payment.getPrice(),order.getProductPost().getPostURL(),
                        payment.getPaymentStatus(),order.getProductPost().getPostId());
                refundLists.add(refundListDto);
            }
            return new ResponseEntity<>(refundLists, HttpStatus.OK);
        } catch (NoSuchElementException e){
            return new ResponseEntity<>("회원 or 주문이 없습니다",HttpStatus.NOT_FOUND);
        } catch (Exception e){
            return new ResponseEntity<>("서버 오류", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> changeNickname(String nickName) {
        String email = getCurrentUserEmail();
        try{
            User user = userRepository.findByEmail(email).orElseThrow(()-> new NoSuchElementException("없는 회원입니다"));
            user.changeNickName(nickName);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchElementException e){
            return new ResponseEntity<>("닉네임 변경 실패 : 없는 유저입니다", HttpStatus.NOT_FOUND);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}


