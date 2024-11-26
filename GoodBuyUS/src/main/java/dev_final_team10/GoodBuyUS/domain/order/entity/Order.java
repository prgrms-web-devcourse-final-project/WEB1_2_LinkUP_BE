package dev_final_team10.GoodBuyUS.domain.order.entity;

import dev_final_team10.GoodBuyUS.domain.BaseEntity;
import dev_final_team10.GoodBuyUS.domain.product.entity.ProductPost;
import dev_final_team10.GoodBuyUS.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

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

    private int quantity;

    @Embedded
    private Address address;

    private int price;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private ProductPost productPost;

    private String needed;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private String payMethod;

    public static Order createOrder(ProductPost productPost, User user, int quantity, Address address, int price, String needed, String payMethod){
        Order order = new Order();
        order.orderName =  user.getName()+"의 "+ productPost.getProduct().getProductName()+" 주문";
        order.quantity = quantity;
        order.address = address;
        order.price = price;
        order.productPost = productPost;
        order.registUser(user);
        order.needed = needed;
        order.orderStatus = OrderStatus.WAITING;
        order.payMethod = payMethod;
        return order;
    }

    public void registUser(User user){
        this.user = user;
        user.getOrders().add(this);
    }

    public void changeOrderStatus(OrderStatus orderStatus){
        this.orderStatus = orderStatus;
    }
}
