package dev_final_team10.GoodBuyUS.domain.order.entity;

import dev_final_team10.GoodBuyUS.domain.BaseEntity;
import dev_final_team10.GoodBuyUS.domain.product.entity.ProductPost;
import dev_final_team10.GoodBuyUS.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.print.DocFlavor;
import java.util.UUID;

@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
public class Order extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "BINARY(16)")
    private UUID orderId;

    private String orderName;

    // 수량
    private int quantity;

    @Embedded
    private Address address;


    private int price;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "post_id")
    private ProductPost productPost;

    private String needed;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Enumerated(EnumType.STRING)
    private Delivery delivery;

    public static Order createOrder(ProductPost productPost, User user, int quantity, Address address, int price, String needed){
        Order order = new Order();
        order.orderName =  user.getName()+"의 "+ productPost.getProduct().getProductName()+" 주문";
        order.quantity = quantity;
        order.address = address;
        order.price = price;
        order.productPost = productPost;
        order.needed = needed;
        order.orderStatus = OrderStatus.WAITING;
        return order;
    }

    public void registUser(User user){
        this.user = user;
        user.getOrders().add(this);
    }

    public void changeOrderStatus(OrderStatus orderStatus){
        this.orderStatus = orderStatus;
    }

    public void defineDelivery(Delivery delivery){
        this.delivery = delivery;
    }
}
