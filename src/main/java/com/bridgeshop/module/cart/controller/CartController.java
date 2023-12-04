package com.bridgeshop.module.cart.controller;

import com.bridgeshop.module.cart.dto.AddToCartRequest;
import com.bridgeshop.module.cart.dto.CartProductDto;
import com.bridgeshop.module.cart.dto.CartProductUpdateRequest;
import com.bridgeshop.module.cart.service.CartService;
import com.bridgeshop.module.cart.entity.Cart;
import com.bridgeshop.module.cart.service.CartProductService;
import com.bridgeshop.module.favorite.entity.Favorite;
import com.bridgeshop.module.favorite.service.FavoriteService;
import com.bridgeshop.module.product.entity.ProductSize;
import com.bridgeshop.module.product.service.ProductService;
import com.bridgeshop.module.product.service.ProductSizeService;
import com.bridgeshop.module.user.service.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {

    private final JwtService jwtService;
    private final CartService cartService;
    private final ProductService productService;
    private final ProductSizeService productSizeService;
    private final CartProductService cartProductService;
    private final FavoriteService favoriteService;

    /**
     * 장바구니 상품 취득
     */
    @GetMapping("/get")
    public ResponseEntity getCartProducts(@CookieValue(value = "token", required = false) String accessToken,
                                          @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                          HttpServletResponse res) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token != null) {
            List<CartProductDto> cartProductDtoList = cartService.getProductsInCart(jwtService.getId(token));
            return new ResponseEntity<>(cartProductDtoList, HttpStatus.OK);

        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 장바구니 상품 추가
     */
//    @PostMapping("/add/by-product-size/{productSizeId}")
//    public ResponseEntity addToCart(@PathVariable("productSizeId") Long productSizeId,
//                                    @CookieValue(value = "token", required = false) String accessToken,
//                                    @CookieValue(value = "refresh_token", required = false) String refreshToken,
//                                    HttpServletResponse res) {
//
//        String token = jwtService.getToken(accessToken, refreshToken, res);
//
//        if (token != null) {
//            ProductSize productSize = productSizeService.retrieveById(productSizeId);
//            cartService.addToCart(jwtService.getId(token), productSize);
//
//            return new ResponseEntity<>(HttpStatus.OK);
//
//        } else {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
//        }
//    }

    /**
     * 장바구니 상품 추가
     */
    @PostMapping("/add/by-product-size")
    public ResponseEntity addToCart(@RequestBody AddToCartRequest addToCartRequest,
                                    @CookieValue(value = "token", required = false) String accessToken,
                                    @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                    HttpServletResponse res) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token != null) {
            ProductSize productSize = productSizeService.retrieveById(addToCartRequest.getProductSizeId());
            int quantity = addToCartRequest.getQuantity();
            cartService.addToCart(jwtService.getId(token), productSize, quantity);

            return new ResponseEntity<>(HttpStatus.OK);

        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 장바구니 상품 추가
     */
    @PostMapping("/add/by-favorite/{favoriteId}")
    public ResponseEntity addFavoriteToCart(@PathVariable("favoriteId") Long favoriteId,
                                            @CookieValue(value = "token", required = false) String accessToken,
                                            @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                            HttpServletResponse res) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token != null) {
            Favorite favorite = favoriteService.retrieveById(favoriteId);
            cartService.addFavoriteToCart(jwtService.getId(token), favorite);

            return new ResponseEntity<>(HttpStatus.OK);

        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 장바구니 상품 수량 변경
     */
    @PostMapping("/update/quantity")
    public ResponseEntity<?> updateQuantity(@RequestBody CartProductUpdateRequest cartProductUpdateRequest,
                                            @CookieValue(value = "token", required = false) String accessToken,
                                            @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                            HttpServletResponse res) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token != null) {
            Cart cart = cartService.retrieveByUserId(jwtService.getId(token));
            cartProductService.updateProductQuantity(cart.getId(), cartProductUpdateRequest.getProductId(), cartProductUpdateRequest.getQuantity());

            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 장바구니 상품 적용 쿠폰 변경
     */
    @PostMapping("/update/coupon")
    public ResponseEntity<?> updateCoupon(@RequestBody CartProductUpdateRequest cartProductUpdateRequest,
                                          @CookieValue(value = "token", required = false) String accessToken,
                                          @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                          HttpServletResponse res) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token != null) {
            cartProductService.updateProductCoupon(cartProductUpdateRequest.getId(), cartProductUpdateRequest.getCouponId());

            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 장바구니 상품 삭제
     */
    @DeleteMapping("/delete/{cartProductId}")
    public ResponseEntity deleteCart(@PathVariable("cartProductId") Long cartProductId,
                                     @CookieValue(value = "token", required = false) String accessToken,
                                     @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                     HttpServletResponse res) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token != null) {
            cartProductService.deleteById(cartProductId);
            return new ResponseEntity<>(HttpStatus.OK);

        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }
}
