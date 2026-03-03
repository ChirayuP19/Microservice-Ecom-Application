package com.app.ecom.repository;

import com.app.ecom.entity.CartItem;
import com.app.ecom.entity.Product;
import com.app.ecom.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem,Long> {

    CartItem findByUserAndProduct(User user, Product product);

    @Modifying
    @Query("DELETE FROM CartItem c WHERE c.user= :user AND c.product = :product")
    int deleteByUserAndProduct(@Param("user") User user, @Param("product") Product product);

    List<CartItem> findByUser(User user);

    void deleteByUser(User user);
}
    
