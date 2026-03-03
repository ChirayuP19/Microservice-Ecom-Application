package com.app.ecom.mapper;

import com.app.ecom.dto.ProductRequest;
import com.app.ecom.dto.ProductResponse;
import com.app.ecom.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product updateProductFromRequest (ProductRequest productRequest, @MappingTarget Product product);

    ProductResponse mapToProductResponse (Product saveProduct);
}
