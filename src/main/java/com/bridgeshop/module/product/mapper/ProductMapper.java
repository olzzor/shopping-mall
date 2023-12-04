package com.bridgeshop.module.product.mapper;

import com.bridgeshop.module.product.dto.ProductDto;
import com.bridgeshop.module.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductMapper {

    public ProductDto mapToDto(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .code(product.getCode())
                .name(product.getName())
                .detail(product.getDetail())
                .price(product.getPrice())
                .discountPer(product.getDiscountPer())
                .status(product.getStatus())
                .regDate(product.getRegDate())
                .modDate(product.getModDate())
                .build();
    }

    public List<ProductDto> mapToDtoList(List<Product> productList) {
        return productList.stream().map(this::mapToDto).collect(Collectors.toList());
    }
}