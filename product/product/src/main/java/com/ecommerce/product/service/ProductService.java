package com.ecommerce.product.service;


import com.ecommerce.product.dto.ProductRequest;
import com.ecommerce.product.dto.ProductResponse;
import com.ecommerce.product.entity.Product;
import com.ecommerce.product.mapper.ProductMapper;
import com.ecommerce.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;


    public ProductResponse createProduct(ProductRequest productRequest) {
        Product product1 = new Product();
        Product product = productMapper.updateProductFromRequest(productRequest,product1);
        Product saveProduct = productRepository.save(product);
        return productMapper.mapToProductResponse(saveProduct);
    }

    public ProductResponse updateProduct(Long id, ProductRequest productRequest) {
         return productRepository.findById(id) // Returns Optional<Product>
                .map(existingProduct -> {
                    // 1. MapStruct updates the fields
                    productMapper.updateProductFromRequest(productRequest, existingProduct);
                    // 2. Save to database
                    Product save = productRepository.save(existingProduct);
                    return productMapper.mapToProductResponse(save); // Return true if successful
                }).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Product not found with id: " + id));
    }

    public  List<ProductResponse> getAllProducts() {
        return productRepository.findByActiveTrue()
                .stream()
                .map(productMapper::mapToProductResponse)
                .collect(Collectors.toList());
    }

    public boolean deleteProduct(Long id) {
        return productRepository.findById(id)
                .map(product->{product.setActive(false);
                productRepository.save(product);
                return true;
                }).orElse(false);
    }

    public List<ProductResponse> searchProducts(String keyword) {
       return  productRepository.searchProducts(keyword)
               .stream()
               .map(productMapper::mapToProductResponse)
               .collect(Collectors.toList());
    }

    public Optional<ProductResponse> getProductById(String id) {
        return productRepository.findByIdAndActiveTrue(Long.valueOf(id))
                .map(productMapper::mapToProductResponse);
    }

//    private ProductResponse mapToProductResponse(Product saveProduct) {
//        ProductResponse product = new ProductResponse();
//        product.setId(saveProduct.getId());
//        product.setName(saveProduct.getName());
//        product.setPrice(saveProduct.getPrice());
//        product.setCategory(saveProduct.getCategory());
//        product.setActive(saveProduct.getActive());
//        product.setDescription(saveProduct.getDescription());
//        product.setImageUrl(saveProduct.getImageUrl());
//        product.setStockQuantity(saveProduct.getStockQuantity());
//        return product;
//
//
//    }

//    private void updateProductFromRequest(Product product,ProductRequest productRequest) {
//        product.setName(productRequest.getName());
//        product.setCategory(productRequest.getCategory());
//        product.setDescription(productRequest.getDescription());
//        product.setStockQuantity(productRequest.getStockQuantity());
//        product.setImageUrl(productRequest.getImageUrl());
//        product.setPrice(productRequest.getPrice());
//    }


}
