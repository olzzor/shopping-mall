package com.bridgeshop.module.product.entity;

import com.bridgeshop.common.entity.BaseTimeEntity;
import com.bridgeshop.module.cart.entity.CartProduct;
import com.bridgeshop.module.category.entity.Category;
import com.bridgeshop.module.coupon.entity.CouponProduct;
import com.bridgeshop.module.favorite.entity.Favorite;
import com.bridgeshop.module.order.entity.OrderDetail;
import com.bridgeshop.module.recentlyviewedproduct.entity.RecentlyViewedProduct;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "products")
public class Product extends BaseTimeEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50, unique = true)
    private String code;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 2000)
    private String detail;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int discountPer;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private ProductStatus status;

    @OneToMany(mappedBy = "product")
    @JsonBackReference
    private List<ProductSize> productSizes = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    @JsonBackReference
    private List<ProductImage> productImages = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    @JsonBackReference
    private List<CartProduct> cartProducts = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    @JsonBackReference
    private List<OrderDetail> orderDetails = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    @JsonBackReference
    private List<Favorite> favorites = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    @JsonBackReference
    private List<CouponProduct> couponProducts = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    @JsonBackReference
    private List<RecentlyViewedProduct> recentlyViewedProducts = new ArrayList<>();

//    @OneToMany(mappedBy = "product")
//    @JsonBackReference
//    private List<Review> reviews = new ArrayList<>();
}
