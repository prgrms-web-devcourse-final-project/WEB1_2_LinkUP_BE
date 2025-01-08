package dev_final_team10.GoodBuyUS.service;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
     * n+1 문제 발생
     * Hibernate: select p1_0.product_id,p1_0.average_rating,p1_0.detail_category,p1_0.product_category,p1_0.product_image,p1_0.product_name,p1_0.product_price,p1_0.reviews_count,p1_0.stock,p1_0.sub_category from product p1_0 where p1_0.product_id=?
     * Hibernate: select r1_0.product_id,r1_0.product_review_id,r1_0.content,r1_0.created_at,r1_0.isused,r1_0.modified_at,r1_0.rating,r1_0.user_id from product_review r1_0 where r1_0.product_id=?
     */
/**
 * 페치 조인으로 쿼리 수 감소, n+1 문제 해결, 리뷰가 없어서 평점은 로딩이 안됨
 */
    public List<ProductPostDTO> findAllProduct(){
        List<ProductPost> productPosts = productPostRepository.findAllWithProductAndReviews();
        List<ProductPostDTO> productPostDTOS = new ArrayList<>();
        for (ProductPost productPost : productPosts) {
            double rate = productPost.calculateAverageStarRating();
            ProductPostDTO productPostDTO = ProductPostDTO.of(productPost, rate);
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
        double rating = setRating(productPost.getProduct());
        for (ProductReview productReview : reviews) {
            PostDetailDTO.ReviewDTO create_reviewDTO = new PostDetailDTO.ReviewDTO();
            create_reviewDTO.setReviewId(productReview.getProductReviewId());
            create_reviewDTO.setContent(productReview.getContent());
            create_reviewDTO.setRating(productReview.getRating());
            create_reviewDTO.setUsing(productReview.isIsused());
            reviewDTOS.add(create_reviewDTO);
        }
        return PostDetailDTO.of(productPost,reviewDTOS, rating);
    }

    // 리뷰 페이지, 구매내역에서 주문 상태를 확인해야함
    public ResponseEntity<?> addReview(String userEmail, ReviewRequestDTO reviewRequestDTO, Long postId) {
        try {
            User user = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new NoSuchElementException("User not found for email: " + userEmail));
            ProductPost productPost = productPostRepository.findById(postId)
                    .orElseThrow(() -> new NoSuchElementException("ProductPost not found for postId: " + postId));
            boolean hasConfirmedPurchase = orderRepository.existsByUserAndProductPostAndOrderStatus(user, productPost, OrderStatus.COMPLETE);
            if (!hasConfirmedPurchase) {
                throw new IllegalStateException("구매 확정을 한 사용자만 리뷰를 남길 수 있습니다.");
            }
            ProductReview productReview = ProductReview.createProductReview(
                    productPost.getProduct(),
                    reviewRequestDTO.getContent(),
                    reviewRequestDTO.getRate(),
                    user.getId());
            reviewRepository.save(productReview);
            productReview.bindReview(productPost.getProduct());
            user.getReviews().add(productReview.getProductReviewId());
            log.info("리뷰 등록 완료 : userEmail : {}", user.getEmail());
            return new ResponseEntity<>("리뷰 등록 완료", HttpStatus.OK);
        } catch (NoSuchElementException e) {
            log.error("유저 또는 게시글을 찾을 수 없음 {}", e.getMessage());
            return new ResponseEntity<>("리뷰 생성 실패",HttpStatus.NOT_FOUND);
        } catch (IllegalStateException e) {
            log.error("해당 유저는 구매한 기록이 없음 {}", e.getMessage());
            return new ResponseEntity<>("리뷰 생성 실패, 구매한 적 없음",HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<>("리뷰 생성 실패",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 리뷰 수정
    public ResponseEntity<?> updateReview(String userEmail, ReviewRequestDTO reviewRequestDTO, Long reviewId){
        try {
            User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NoSuchElementException("해당 이메일을 사용하는 유저가 없습니다.: " + userEmail));
             ProductReview productReview = reviewRepository.findByUserIdAndProductReviewId(user.getId(), reviewId)
                .orElseThrow(()-> new NoSuchElementException("기존에 작성한 리뷰가 없습니다."));
            // 리뷰 내용 및 별점 변경
            productReview.changeContent(reviewRequestDTO.getContent());
            productReview.changeRating(reviewRequestDTO.getRate());
            log.info("리뷰를 변경한 사용자 {}, 변경 내용 {}, 별점 수정{}", userEmail, reviewRequestDTO.getContent(), reviewRequestDTO.getRate());
            // 성공적으로 처리된 경우
            return ResponseEntity.status(HttpStatus.OK).body("리뷰 변경 성공");
        }catch (NoSuchElementException e){
            log.error("리뷰 변경 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (RuntimeException e){
            log.error("리뷰 변경 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }catch (Exception e){
            log.error("리뷰 변경 실패 : {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // 리뷰 삭제 - 논리
    public ResponseEntity<?> deleteReview(String userEmail, Long reviewId) {
        try {
            // 사용자 조회
            User user = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new NoSuchElementException("User not found for email: " + userEmail));
            // 리뷰 조회
            ProductReview productReview = reviewRepository.findByUserIdAndProductReviewId(user.getId(), reviewId)
                    .orElseThrow(() -> new NoSuchElementException("기존에 작성한 리뷰가 없습니다."));
            // 리뷰 논리적 삭제
            productReview.removeReview();
            // 성공 응답
            return new ResponseEntity<>("리뷰 삭제 성공", HttpStatus.OK);
        } catch (NoSuchElementException e) {
            // 사용자 또는 리뷰가 없을 때
            log.error("리뷰 삭제 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("리뷰 또는 사용자를 찾을 수 없습니다.");
        } catch (Exception e) {
            // DB 오류나 트랜잭션 실패 등
            log.error("리뷰 삭제 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("리뷰 삭제에 실패했습니다. 잠시 후 다시 시도해 주세요.");
        }
    }

    //리뷰 삭제 - 물리
    public void deleteReview(Long reviewId, String userEmail){
        try{
            // 사용자 조회
            User user = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new NoSuchElementException("User not found for email: " + userEmail));
            ProductReview removeReview = reviewRepository.findById(reviewId)
                    .orElseThrow(()-> new NoSuchElementException("작성된적 없는 리뷰입니다."));
            user.getReviews().remove(reviewId);
            userRepository.save(user);
            reviewRepository.delete(removeReview);
        } catch (NoSuchElementException e){
            log.error("작성이 안돼서 지울게 없음"+e.getMessage());
        } catch (RuntimeException e){
            log.error("DB 문제"+e.getMessage());
        }
    }

    public double setRating(Product product) {
        // 상태가 true인 리뷰만 필터링
        List<ProductReview> reviews = product.getReviews()
                .stream()
                .filter(ProductReview::isIsused)
                .toList();
        if (reviews.isEmpty()) {
            return 0.0;
        }
        // 평균 점수를 소수점 첫째 자리까지 계산
        double averageRating = reviews.stream()
                .mapToDouble(ProductReview::getRating) // 점수 추출
                .average()
                .orElse(0.0);
        return Math.round(averageRating * 10) / 10.0;
    }
}
