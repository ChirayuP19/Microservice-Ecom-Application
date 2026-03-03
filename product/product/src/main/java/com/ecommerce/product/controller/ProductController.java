package com.ecommerce.product.controller;


import com.ecommerce.product.dto.ProductRequest;
import com.ecommerce.product.dto.ProductResponse;
import com.ecommerce.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping("")
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest productRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(productRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id,@RequestBody ProductRequest productRequest){
        return  ResponseEntity.status(HttpStatus.OK).body(productService.updateProduct(id,productRequest));
    }

    @GetMapping("")
    public ResponseEntity<List<ProductResponse>>getAllData(){
        return ResponseEntity.status(HttpStatus.OK).body(productService.getAllProducts());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id){
        boolean b = productService.deleteProduct(id);
        return b ? ResponseEntity.noContent().build(): ResponseEntity.notFound().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductResponse>> searchProducts(@RequestParam String keyword){
        return ResponseEntity.status(HttpStatus.OK).body(productService.searchProducts(keyword));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById( @PathVariable  String id){
        return productService.getProductById(id).map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
    }



}
