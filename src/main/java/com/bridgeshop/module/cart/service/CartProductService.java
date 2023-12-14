package com.bridgeshop.module.cart.service;

import com.bridgeshop.common.exception.NotFoundException;
import com.bridgeshop.module.cart.dto.CartProductDto;
import com.bridgeshop.module.cart.entity.CartProduct;
import com.bridgeshop.module.cart.mapper.CartProductMapper;
import com.bridgeshop.module.cart.repository.CartProductRepository;
import com.bridgeshop.module.cart.repository.CartRepository;
import com.bridgeshop.module.category.dto.CategoryDto;
import com.bridgeshop.module.category.service.CategoryService;
import com.bridgeshop.module.coupon.dto.CouponDto;
import com.bridgeshop.module.coupon.entity.Coupon;
import com.bridgeshop.module.coupon.repository.CouponRepository;
import com.bridgeshop.module.coupon.service.CouponService;
import com.bridgeshop.module.favorite.repository.FavoriteRepository;
import com.bridgeshop.module.product.dto.ProductDto;
import com.bridgeshop.module.product.dto.ProductSizeDto;
import com.bridgeshop.module.product.entity.Product;
import com.bridgeshop.module.product.entity.ProductSize;
import com.bridgeshop.module.product.mapper.ProductSizeMapper;
import com.bridgeshop.module.product.service.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartProductService {

    private final ProductService productService;
    private final CouponService couponService;
    private final CategoryService categoryService;
    private final CartRepository cartRepository;
    private final CartProductRepository cartProductRepository;
    private final FavoriteRepository favoriteRepository;
    private final CouponRepository couponRepository;
    private final CartProductMapper cartProductMapper;
    private final ProductSizeMapper productSizeMapper;


    public CartProduct retrieveById(Long id) {
        return cartProductRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("cartProductNotFound", "장바구니 상품 정보를 찾을 수 없습니다."));
    }

    public CartProductDto getCartProductDto(CartProduct cartProduct) {
        Product product = cartProduct.getProduct();
        ProductSize productSize = cartProduct.getProductSize();

        CartProductDto cartProductDto = cartProductMapper.mapToDto(cartProduct);

        ProductDto productDto = productService.getDtoWithMainImage(product);
        CategoryDto categoryDto = categoryService.convertToDto(product.getCategory());
        productDto.setCategory(categoryDto);
        ProductSizeDto productSizeDto = productSizeMapper.mapToDto(productSize);

        cartProductDto.setProduct(productDto);
        cartProductDto.setProductSize(productSizeDto);

        if (cartProduct.getCoupon() != null) {
            CouponDto couponDto = couponService.convertToDto(cartProduct.getCoupon());
            cartProductDto.setCoupon(couponDto);
        }

        return cartProductDto;
    }

    public List<CartProductDto> getCartProductDtoList(List<CartProduct> cartProductList) {
        return cartProductList.stream().map(this::getCartProductDto).collect(Collectors.toList());
    }

    public boolean checkStock(Long userId) {
        Long cartId = cartRepository.findIdByUser_Id(userId)
                .orElseThrow(() -> new NotFoundException("cartNotFound", "장바구니 정보를 찾을 수 없습니다."));

        List<CartProduct> cartProductList = cartProductRepository.findAllByCart_Id(cartId);

        if (cartProductList.isEmpty()) {
            throw new NotFoundException("cartProductEmpty", "장바구니에 상품이 없습니다.");
        }

        for (CartProduct cartProduct : cartProductList) {
            if (cartProduct.getQuantity() > cartProduct.getProductSize().getQuantity()) {
                return false; // 재고가 부족한 경우
            }
        }
        return true; // 모든 상품에 대해 재고가 충분한 경우
    }

    @Transactional
    public void updateProductQuantity(Long cartId, Long productId, int quantity) {
        CartProduct cartProduct = cartProductRepository.findByCart_IdAndProduct_Id(cartId, productId)
                .orElseThrow(() -> new NotFoundException("cartProductNotFound", "장바구니 상품 정보를 찾을 수 없습니다."));
        cartProduct.setQuantity(quantity);
        cartProductRepository.save(cartProduct);
    }

    @Transactional
    public void updateProductCoupon(Long cartProductId, Long couponId) {
        CartProduct cartProduct = cartProductRepository.findById(cartProductId)
                .orElseThrow(() -> new NotFoundException("cartProductNotFound", "장바구니 상품 정보를 찾을 수 없습니다."));
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new NotFoundException("couponNotFound", "쿠폰 정보를 찾을 수 없습니다."));
        cartProduct.setCoupon(coupon);
        cartProductRepository.save(cartProduct);
    }

    @Transactional
    public void deleteById(Long cartProductId) {
        cartProductRepository.deleteById(cartProductId);
    }

    @Transactional
    public void deleteByCartId(Long cartId) {
        cartProductRepository.deleteByCart_Id(cartId);
    }
}