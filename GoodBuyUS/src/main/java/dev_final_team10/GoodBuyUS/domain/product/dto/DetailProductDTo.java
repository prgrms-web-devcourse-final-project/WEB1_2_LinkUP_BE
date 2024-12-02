package dev_final_team10.GoodBuyUS.domain.product.dto;

import dev_final_team10.GoodBuyUS.domain.product.entity.ProductPost;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class DetailProductDTo {
    private String url;
    private String productName;
    // 가격은 그냥 할인가로 일괄 구매
    private int price;
    private int amount;
    // 개별 * 수량 가격
    private int finalPrice;

    public static DetailProductDTo createDTO(ProductPost productPost, int amount){
        DetailProductDTo orderResponseDTO = new DetailProductDTo();
        orderResponseDTO.setUrl(productPost.getPostURL());
        orderResponseDTO.setProductName(productPost.getTitle());
        orderResponseDTO.setPrice(productPost.getProuctDiscount());
        orderResponseDTO.setAmount(amount);
        orderResponseDTO.setFinalPrice(orderResponseDTO.price * amount) ;
        return orderResponseDTO;
    }
}
