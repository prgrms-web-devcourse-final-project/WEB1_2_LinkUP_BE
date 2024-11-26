package dev_final_team10.GoodBuyUS.service;
import dev_final_team10.GoodBuyUS.domain.order.dto.OrderRequestDTO;
import dev_final_team10.GoodBuyUS.domain.product.dto.OrderResponseDTO;
import dev_final_team10.GoodBuyUS.domain.product.entity.ProductPost;
import dev_final_team10.GoodBuyUS.domain.product.dto.PostDetailDTO;
import dev_final_team10.GoodBuyUS.domain.product.dto.ProductPostDTO;
import dev_final_team10.GoodBuyUS.domain.product.entity.ProductReview;
import dev_final_team10.GoodBuyUS.repository.ProductPostRepository;
import lombok.RequiredArgsConstructor;
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
@Transactional
@RequiredArgsConstructor
public class ProductPostService {
    private final ProductPostRepository productPostRepository;

    /**
     * 전체 판매 리스트 전달(썸네일), 쿼리 최적화 필요
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
            create_reviewDTO.setContent(productReview.getContent());
            create_reviewDTO.setRating(productReview.getRating());
            reviewDTOS.add(create_reviewDTO);
        }
        return PostDetailDTO.of(productPost,reviewDTOS);
    }

    /**
     * 결제하기를 눌렀을 때 주문서에 데이터가 들어갈 수 있도록 데이터를 전달해야함
     * 만약 구매수량이 게시글의 최소 수량보다 적다면 original price를 리턴하고 구매수량이 게시글의 최소 수량과 같거나 크다면 discount price를 리턴한다.
     * @return
     */
    public OrderResponseDTO requestPayment(OrderRequestDTO orderRequestDTO, Long postId){
        ProductPost productPost = productPostRepository.findById(postId).orElseThrow(()-> new NoSuchElementException("없는 게시글입니다."));
        OrderResponseDTO orderResponseDTO = new OrderResponseDTO();
        orderResponseDTO.setProductName(orderRequestDTO.getProductName());
        orderResponseDTO.setUrl(orderRequestDTO.getUrl());
        orderResponseDTO.setAmount(orderRequestDTO.getAmount());
        orderResponseDTO.setPostId(postId);

        // 주문 최소 개수가 미니멈보다 적을 때 원가로 결제해야함
        if(orderRequestDTO.getAmount() < productPost.getMinAmount()){
            orderResponseDTO.setPrice(productPost.getOriginalPrice());
            orderResponseDTO.setFinalPrice(orderRequestDTO.getAmount()*orderRequestDTO.getPrice());
        }
        else{ orderResponseDTO.setPrice(productPost.getProuctDiscount());
        orderResponseDTO.setFinalPrice(orderRequestDTO.getAmount()*orderRequestDTO.getDiscountPrice());}
        return orderResponseDTO;
    }
}
