package dev_final_team10.GoodBuyUS.service;


import dev_final_team10.GoodBuyUS.domain.community.dto.PostResponseDto;
import dev_final_team10.GoodBuyUS.domain.community.dto.WriteModifyPostDto;
import dev_final_team10.GoodBuyUS.domain.community.entity.CommunityCategory;
import dev_final_team10.GoodBuyUS.domain.community.entity.CommunityPost;
import dev_final_team10.GoodBuyUS.domain.order.dto.OrdersDTO;
import dev_final_team10.GoodBuyUS.domain.order.entity.Order;
import dev_final_team10.GoodBuyUS.domain.payment.entity.MainPayment;
import dev_final_team10.GoodBuyUS.domain.user.entity.Neighborhood;
import dev_final_team10.GoodBuyUS.domain.user.entity.User;
import dev_final_team10.GoodBuyUS.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
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

    //커뮤니티에 작성한 글 수정하는 메소드
    public PostResponseDto modifyPost(WriteModifyPostDto writeModifyPostDto, Long communityPostId) {
        CommunityPost communityPost = communityPostRepository.findById(communityPostId).orElse(null);
        //현재 사용자 정보 가져오기(글 작성자)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName()).orElse(null);
        //현재 사용자 동네 정보 가져오기
        Neighborhood neighborhood = user.getNeighborhood();
        //카테고리 설정
        CommunityCategory communityCategory = CommunityCategory.fromString(writeModifyPostDto.getCategory());

//        communityPost.updateFields(writeModifyPostDto, user, neighborhood, communityCategory);
        //DB 저장
        communityPostRepository.save(communityPost);

        return PostResponseDto.of(communityPost);

    }

    //내가 쓴 글 목록 보기
    public List<PostResponseDto> myPostList() {
        //현재 사용자 정보 가져오기
        User user = userRepository.findByEmail(getCurrentUserEmail()).orElse(null);

        //현재 사용자 글 목록 가져오기
        List<CommunityPost> communityPosts = communityPostRepository.findAllByUserId(user.getId());

        List<PostResponseDto> postResponseDtos = new ArrayList<>();

        for (CommunityPost communityPost : communityPosts) {
            postResponseDtos.add(PostResponseDto.of(communityPost));
        }
            return postResponseDtos;
    }

    // 주문 내역 확인하기
    public List<OrdersDTO> orderlist(String userEmail){
        List<Order> orders = orderRepository.findOrderByUser(userRepository.findByEmail(userEmail).orElseThrow(()->new NoSuchElementException("없는 유저")));
        List<OrdersDTO> ordersDTOS = new ArrayList<>();
        for (Order order : orders) {
            MainPayment payment = mainPaymentRepository.findByOrder(order).orElseThrow(null);
            OrdersDTO ordersDTO = OrdersDTO.of(order.getOrderName(),order.getPrice(), order.getCreatedAt(),payment.getPaymentStatus(),payment.getPaymentKey(),order.getQuantity(), order.getDelivery());
            ordersDTOS.add(ordersDTO);
        }
        return ordersDTOS;
    }
}
