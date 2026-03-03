package com.app.ecom.service;

import com.app.ecom.dto.CartItemRequest;
import com.app.ecom.entity.CartItem;
import com.app.ecom.entity.Product;
import com.app.ecom.entity.User;
import com.app.ecom.repository.CartItemRepository;
import com.app.ecom.repository.ProductRepository;
import com.app.ecom.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public boolean addToCart(String userID, CartItemRequest request) {
        Optional<Product> productOpt = productRepository.findById(request.getProductId());
        if(productOpt.isEmpty())
            return false;

        Product product = productOpt.get();
        if (product.getStockQuantity()< request.getQuantity())
            return false;
        Optional<User> userOpt = userRepository.findById(Long.valueOf(userID));
        if(userOpt.isEmpty())
            return false;

        User user = userOpt.get();
       CartItem exsistingCartItem = cartItemRepository.findByUserAndProduct(user,product);
       if(exsistingCartItem != null){
           exsistingCartItem.setQuantity(exsistingCartItem.getQuantity()+ request.getQuantity());
           exsistingCartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(exsistingCartItem.getQuantity())));
           cartItemRepository.save(exsistingCartItem);
       }else {

           CartItem cartItem = new CartItem();
           cartItem.setProduct(product);
           cartItem.setUser(user);
           cartItem.setQuantity(request.getQuantity());
           cartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(request.getQuantity())));
           cartItemRepository.save(cartItem);
       }
       return true;
    }

    public boolean deleteItemFromCart(String userId, Long productId) {
        try {


        Optional<Product> productOpt = productRepository.findById(productId);
        Optional<User> userOpt = userRepository.findById(Long.valueOf(userId));

        if(userOpt.isPresent() && productOpt.isPresent()){
            int rowsDeleted = cartItemRepository.deleteByUserAndProduct(userOpt.get(), productOpt.get());
            if (rowsDeleted > 0) {
                log.info("Successfully deleted product {} from user {}'s cart", productId, userId);
                return true;
            } else {
                log.warn("Entities exist, but no matching CartItem found for UserId: {} and ProductId: {}", userId, productId);
                return false;
            }
        }
        }catch (NumberFormatException e){
            log.error("Invalid User ID format: {}", userId);
        }
        return false;
    }


    public List<CartItem> getCart(String userId) {
        log.debug("Fetching cart for user: {}", userId);
        return userRepository.findById(Long.valueOf(userId))
                .map(user -> {
                    List<CartItem> items = cartItemRepository.findByUser(user);
                    log.info("User {} found with {} items",userId,items.size());
                    return items;
                }).orElseGet(()->{
                    log.warn("Cart requested for non-existent user: {}", userId);
                    return List.of();
                });
    }

    public void clearCart(String userId) {
        userRepository.findById(Long.valueOf(userId))
                .ifPresent(cartItemRepository::deleteByUser);

    }
}
