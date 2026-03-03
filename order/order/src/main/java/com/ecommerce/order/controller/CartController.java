package com.ecommerce.order.controller;


import com.ecommerce.order.dto.CartItemRequest;
import com.ecommerce.order.entity.CartItem;
import com.ecommerce.order.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("")
    public ResponseEntity<String> addToCart(
            @RequestHeader("X-User-Id") String userID,
            @RequestBody CartItemRequest request){
        if (!cartService.addToCart(userID,request)) {
            return ResponseEntity.badRequest().body("Not able to complete request !");
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<Void> removeFromCart(
            @RequestHeader("X-User-Id") String userId,
            @PathVariable String productId){
         boolean deleted = cartService.deleteItemFromCart(userId,productId);
         return deleted ? ResponseEntity.noContent().build(): ResponseEntity.notFound().build();
    }

    @GetMapping("")
    public ResponseEntity<List<CartItem>> getCart(
            @RequestHeader("X-User-Id") String userId){
        return ResponseEntity.status(HttpStatus.OK).body(cartService.getCart(userId));
    }
}

