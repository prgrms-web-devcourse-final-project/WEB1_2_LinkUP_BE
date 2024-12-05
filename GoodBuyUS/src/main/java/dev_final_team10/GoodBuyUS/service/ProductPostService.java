package dev_final_team10.GoodBuyUS.service;
import dev_final_team10.GoodBuyUS.domain.order.entity.Order;
import dev_final_team10.GoodBuyUS.domain.order.entity.OrderStatus;
import dev_final_team10.GoodBuyUS.domain.product.dto.ReviewRequestDTO;
import dev_final_team10.GoodBuyUS.domain.product.entity.Product;
import dev_final_team10.GoodBuyUS.domain.product.entity.ProductPost;
import dev_final_team10.GoodBuyUS.domain.product.dto.PostDetailDTO;
import dev_final_team10.GoodBuyUS.domain.product.dto.ProductPostDTO;
import dev_final_team10.GoodBuyUS.domain.product.entity.ProductReview;
import dev_final_team10.GoodBuyUS.domain.user.entity.User;
import dev_final_team10.GoodBuyUS.repository.OrderRepository;
import dev_final_team10.GoodBuyUS.repository.ProductPostRepository;
import dev_final_team10.GoodBuyUS.repository.ProductReviewRepository;
import dev_final_team10.GoodBuyUS.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.service.invoker.HttpServiceArgumentResolver;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;


/**
 * Todo
 * 1. 재고가 0일 때 판매 중지 : 구매하는 만큼 수량을 줄이고 수량이 0일 버튼 비활성화 시켜야함 > 어떻게하지?
 * 2. 마감기한이 다 됐을 때 판매 중지 : 어떻게 하지 ?
 * 3. 리뷰 개수 카운팅해서 상품에 평균 평점 남기기 - 성능 개선 필요
 * 4. 리뷰 남겼을 때 반영되나 확인하기
 * 5. 이슈 : 리뷰, 상품 구매 모두 동일하게 다른 사람의 화면에서도 변경된 숫자가 반영되어야함 - 주기적 풀링
 * 6. 웹소켓을 통한 상품 재고 실시간 관리
 */
@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ProductPostService {
    private final ProductPostRepository productPostRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductReviewRepository reviewRepository;

    /**
     * 전체 판매 리스트 전달(썸네일), 쿼리 최적화 필요
     * 현재 판매 중인 것들만!
     */
    public List<ProductPostDTO> findAllProduct(){
        List<ProductPost> productPosts = productPostRepository.findAll();
        List<ProductPostDTO> productPostDTOS = new ArrayList<>();
        for (ProductPost productPost : productPosts) {
            ProductPostDTO productPostDTO = ProductPostDTO.of(productPost);
            productPostDTOS.add(productPostDTO);
        }
        return productPostDTOS;
    }

    /**
     * 상세 내용 페이지
     * 쿼리 간단화할 수 있으면 좋겠음, 지금 reviews를 쓰는 이유가 productPost에 rating과 리뷰가 없기 때문인데 두 테이블을 페치 조인한다면 쿼리 하나로 해결할 수 있을 것 같음
     */
    public PostDetailDTO findPost(Long postId){
        ProductPost productPost = productPostRepository.findById(postId).orElseThrow(()-> new NoSuchElementException("없는 게시글입니다."));
        List <ProductReview> reviews = productPost.getProduct().getReviews();
        List<PostDetailDTO.ReviewDTO> reviewDTOS = new ArrayList<>();
        for (ProductReview productReview : reviews) {
            PostDetailDTO.ReviewDTO create_reviewDTO = new PostDetailDTO.ReviewDTO();
            create_reviewDTO.setReviewId(productReview.getProductReviewId());
            create_reviewDTO.setContent(productReview.getContent());
            create_reviewDTO.setRating(productReview.getRating());
            create_reviewDTO.setUsing(productReview.isIsused());
            reviewDTOS.add(create_reviewDTO);
        }
        return PostDetailDTO.of(productPost,reviewDTOS);
    }

    // 리뷰 페이지, 구매내역에서 주문 상태를 확인해야함
    public ResponseEntity<?> addReview(String userEmail, ReviewRequestDTO reviewRequestDTO, Long postId){
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NoSuchElementException("User not found for email: " + userEmail));
        ProductPost productPost = productPostRepository.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("ProductPost not found for postId: " + postId));
        boolean hasConfirmedPurchase = orderRepository.existsByUserAndProductPostAndOrderStatus(user,productPost,OrderStatus.COMPLETE);
        if(!hasConfirmedPurchase){
            throw new IllegalStateException("구매 확정을 한 사용자만 리뷰를 남길 수 있습니다.");
        }
        try{
            ProductReview productReview = ProductReview.createProductReview(
                    productPost.getProduct(),
                    reviewRequestDTO.getContent(),
                    reviewRequestDTO.getRate(),user);
            reviewRepository.save(productReview);
            log.info("리뷰 등록 완료 : userEmail : {}",user.getEmail());
            return new ResponseEntity<>("리뷰 등록 완료", HttpStatus.OK);
        } catch (Exception e){
            log.error("리뷰 등록 실패 : {}", e.getMessage());
            throw new RuntimeException("리뷰 등록 실패 : " + e.getMessage());
        }
    }

    // 리뷰 수정
    public ResponseEntity<?> updateReview(String userEmail, ReviewRequestDTO reviewRequestDTO, Long reviewId){
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NoSuchElementException("User not found for email: " + userEmail));
        ProductReview productReview = reviewRepository.findByUserAndProductReviewId(user, reviewId)
                .orElseThrow(()-> new NoSuchElementException("기존에 작성한 리뷰가 없습니다."));
        try
        {
            productReview.changeContent(reviewRequestDTO.getContent());
            productReview.changeRating(reviewRequestDTO.getRate());
            log.info("리뷰를 변경한 사용자 {}, 변경 내용 {}, 별점 수정{}",userEmail,reviewRequestDTO.getContent(),reviewRequestDTO.getRate());
            return new ResponseEntity<>("리뷰 변경 성공", HttpStatus.OK);
        } catch (RuntimeException e){
            log.error("리뷰 변경 실패: {}", e.getMessage());
            throw new RuntimeException("리뷰 변경 실패 : " + e.getMessage());
        }
    }

    public ResponseEntity<?> deleteReview(String userEmail, Long reviewId) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NoSuchElementException("User not found for email: " + userEmail));
        ProductReview productReview = reviewRepository.findByUserAndProductReviewId(user, reviewId)
                .orElseThrow(() -> new NoSuchElementException("기존에 작성한 리뷰가 없습니다."));
        try {
            productReview.removeReview();
            return new ResponseEntity<>("리뷰 삭제 성공", HttpStatus.OK);
        } catch (Exception e) {
            // 예외 처리 (DB 오류, 트랜잭션 실패 등)
            log.error("리뷰 삭제 실패: {}", e.getMessage());
            throw new RuntimeException("리뷰 삭제에 실패했습니다. 잠시 후 다시 시도해 주세요.");
        }
    }
}
