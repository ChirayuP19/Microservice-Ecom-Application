package com.ecommerce.order.repository;


import com.ecommerce.order.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem,Long> {

    CartItem findByUserIdAndProductId(String userId, String productId);

    @Modifying
    @Query("DELETE FROM CartItem c WHERE c.userId= :user AND c.productId = :product")
    int deleteByUserIdAndProductId(@Param("user") String userId, @Param("product") String productId);

    List<CartItem> findByUserId(String userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM CartItem c WHERE c.userId = :userId")
    void deleteByUserId(@Param("userId")String userId);
}
    
