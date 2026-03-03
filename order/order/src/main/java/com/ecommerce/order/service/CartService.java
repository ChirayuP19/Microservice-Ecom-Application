package com.ecommerce.order.service;


import com.ecommerce.order.client.ProductServiceClient;
import com.ecommerce.order.client.UserServiceClient;
import com.ecommerce.order.dto.CartItemRequest;
import com.ecommerce.order.dto.ProductResponse;
import com.ecommerce.order.dto.UserResponse;
import com.ecommerce.order.entity.CartItem;
import com.ecommerce.order.repository.CartItemRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final ProductServiceClient productServiceClient;
    private final UserServiceClient userServiceClient;
    int attempt = 0;

    @CircuitBreaker(name = "productService",fallbackMethod = "addToCartFallBack")
//    @Retry(name = "retryBreaker", fallbackMethod = "addToCartFallBack")
    public boolean addToCart(String userID, CartItemRequest request) {
    System.out.println("ATTEMPT COUNT: "+ ++attempt);

        ProductResponse productResponse = productServiceClient.getProductDetails(request.getProductId());
        if(productResponse == null || productResponse.getStockQuantity()< request.getQuantity())
            return false;

        UserResponse userResponse = userServiceClient.getUserDetails(userID);
        if(userResponse==null)
            return false;

        CartItem exsistingCartItem = cartItemRepository.findByUserIdAndProductId(userID, request.getProductId());
       if(exsistingCartItem != null){
           exsistingCartItem.setQuantity(exsistingCartItem.getQuantity()+ request.getQuantity());
           exsistingCartItem.setPrice(BigDecimal.valueOf(1000.00));
           cartItemRepository.save(exsistingCartItem);
       }else {

           CartItem cartItem = new CartItem();
           cartItem.setProductId(request.getProductId());
           cartItem.setUserId(userID);
           cartItem.setQuantity(request.getQuantity());
           cartItem.setPrice(BigDecimal.valueOf(1000.00));
           cartItemRepository.save(cartItem);
       }
       return true;
    }

    public boolean addToCartFallBack(String userID,
                                     CartItemRequest request ,
                                     Exception exception){
        exception.printStackTrace();
        return false;
    }

    public boolean deleteItemFromCart(String userId, String productId) {

        CartItem cartItem =cartItemRepository.findByUserIdAndProductId(userId,productId);

        if(cartItem !=null){
            cartItemRepository.delete(cartItem);
                log.info("Successfully deleted product {} from user {}'s cart", productId, userId);
                return true;
        }

        log.warn("Entities exist, but no matching CartItem found for UserId: {} and ProductId: {}", userId, productId);
        return false;
    }


    public List<CartItem> getCart(String userId) {
        log.debug("Fetching cart for user: {}", userId);
        return cartItemRepository.findByUserId(userId);
    }

    public void clearCart(String userId) {
       cartItemRepository.deleteByUserId(userId);

    }
}
