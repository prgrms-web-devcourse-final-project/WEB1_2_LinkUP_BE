package dev_final_team10.GoodBuyUS.service;

import dev_final_team10.GoodBuyUS.domain.order.dto.*;
import dev_final_team10.GoodBuyUS.domain.order.entity.Order;
import dev_final_team10.GoodBuyUS.domain.order.entity.OrderStatus;
import dev_final_team10.GoodBuyUS.domain.product.dto.OrderResponseDTO;
import dev_final_team10.GoodBuyUS.domain.product.entity.ProductPost;
import dev_final_team10.GoodBuyUS.domain.user.entity.User;
import dev_final_team10.GoodBuyUS.repository.OrderRepository;
import dev_final_team10.GoodBuyUS.repository.ProductPostRepository;
import dev_final_team10.GoodBuyUS.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductPostRepository productPostRepository;
    private final UserRepository userRepository;
    /**
     * 주문하기 주문하는 포맷은 저장할 필요가 없음, 구매 후 구매 목록만 저장하면 됨
     * Todo
     * 1. 주문서 만들기, 따로 저장할 필요 없음 -> 한번에 처리할 수 없나 ?
     * 2. 주문서를 만들고 결제 요청을 보낸 뒤 결제가 되면 구매 확정 목록을 DB에 저장
     * 3. 결제 만들기까지 엔드포인트 명세 완료
     * 4. 리뷰 작업 crud 테스트
     * @param userId
     * @param postId
     * @return
     */
    /**
     * 주문 엔티티 만들기
     * @param orderRequestDTO
     * @return
     */
    /**
     * 주문 생성
     * 주문 상태 : 3가지 (기본이 waiting)
     * 질문 : 폼 데이터가 다 채워지는 순간 바로 저장, 결제 상태는 대기 -> 언제든 내가 이전에 주문하려고했던걸 불러올 수 있음
     * 질문 : 전 페이지에서 사용자가 입력한 객체를 한번 더 쓸 수 있나요 ? 반드시 물어볼 것
     * 아래 : 일단 필드가 다 채워지면 바로 저장
     * 하나의 로직으로 통합, 만약 게시글의 최소 판매 수량이 넘었다면 discount로 오더를 만들어도 되지만 그렇지 않다면 ? 최소 수량이 아닌 원래 가격으로 구매해야함
     * 일단 할인된 가격으로 전부 구매하게하고 추후에 수정이 있어야할 것 같음, 어떻게 처리하는가 ? 환불하는가 ?
     * 만약 최소 구매 갯수를 충족시키지 않으면 할인 안된 가격으로 구매해야함
     */
    public UUID requestPayment(PaymentRequestDTO paymentRequestDTO, String userEmail) {
        ProductPost productPost = productPostRepository.findById(paymentRequestDTO.getPostId()).orElseThrow(() -> new NoSuchElementException("잘못된 게시글입니다 - 주문"));
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new NoSuchElementException("없는 회원"));;
        // 검증 1 : 구매하려는 수량이 게시글의 수량보다 많은가 ? 많으면 반려
        // 검증 2 : 구매하려는 시점이 최소 구매 물량을 넘긴 시점의 가격인가 ?
        Order order = Order.createOrder(productPost, user, paymentRequestDTO.getAmount(), paymentRequestDTO.getAddress(),
                paymentRequestDTO.getFinalPrice(), paymentRequestDTO.getNeeded(), paymentRequestDTO.getPayMethod());
        orderRepository.save(order);
        return order.getOrderId();
    }
    // 결제 : 결제 방법 선택 후 진짜 결제

    /**
     * Json 형식 이상함, 테스트는 그냥 결제 오케이 로직이 돌았을 때 오더의 상태를 완료로 변경. 이를 통해 리뷰를 작업할 수 있음
     */
    public PaymentResponseDTO<?> payment(TossRequestDTO tossRequestDTO){
        if(tossRequestDTO!=null){
            Order order = orderRepository.findById(tossRequestDTO.getOrderId()).orElseThrow(()->new NoSuchElementException("잘못된 게시글입니다 - 주문"));
            order.changeOrderStatus(OrderStatus.COMPLETE); //주문 성공
            return new PaymentResponseDTO<>(new SuccessPaymentResponseDTO(tossRequestDTO.getPostId(), tossRequestDTO.getOrderId(),tossRequestDTO.getAmount()));
        }
        else return new PaymentResponseDTO<>(new FailedPaymentResponseDTO("결제 실패 오류 null"));
    }

    public TossRequestDTO matchTossRequest(UUID orderid) {
        TossRequestDTO tossRequestDTO = new TossRequestDTO();
        Order order = orderRepository.findById(orderid).orElseThrow(()->new NoSuchElementException("없는 오더입니다"));
        tossRequestDTO.setOrderId(order.getOrderId());
        tossRequestDTO.setAmount(order.getPrice());
        tossRequestDTO.setPostId(order.getProductPost().getPostId());
        tossRequestDTO.setUserId(order.getUser().getId());
        tossRequestDTO.setQuantity(order.getQuantity());
        return tossRequestDTO;
    }
}
