package dev_final_team10.GoodBuyUS.repository;

import dev_final_team10.GoodBuyUS.domain.order.entity.Order;
import dev_final_team10.GoodBuyUS.domain.product.entity.Product;
import dev_final_team10.GoodBuyUS.domain.product.entity.ProductPost;
import dev_final_team10.GoodBuyUS.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    public List<Order> findOrderByUser(User user);
    public List<Order> findAllByProductPost(ProductPost product);
}
