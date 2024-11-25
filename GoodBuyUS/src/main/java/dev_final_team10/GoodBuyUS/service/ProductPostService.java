package dev_final_team10.GoodBuyUS.service;
import dev_final_team10.GoodBuyUS.domain.Product;
import dev_final_team10.GoodBuyUS.domain.ProductPost;
import dev_final_team10.GoodBuyUS.domain.ProductReview;
import dev_final_team10.GoodBuyUS.dto.productpost.ProductPostDTO;
import dev_final_team10.GoodBuyUS.repository.ProductPostRepository;
import dev_final_team10.GoodBuyUS.repository.ProductRepository;
import dev_final_team10.GoodBuyUS.repository.ProductReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


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
     * 상세 내용 페이지(3개의 API로 구성 1. 상품 정보 제공 2. 리뷰 3. 주문)
     */


}
