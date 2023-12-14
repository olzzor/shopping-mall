package com.bridgeshop.module.product.entity;

import com.bridgeshop.common.entity.BaseTimeEntity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "product_images")
public class ProductImage extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(nullable = false, name = "product_id", referencedColumnName = "id")
    private Product product;

    @Column(nullable = false, length = 100)
    private String filePath;

    @Column(nullable = false, length = 100)
    private String fileName;

    @Column(nullable = false, length = 10)
    private int displayOrder;

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public ProductImage(Product product, String filePath, String fileName, int displayOrder) {
        this.product = product;
        this.filePath = filePath;
        this.fileName = fileName;
        this.displayOrder = displayOrder;
    }

    // 설정자 메서드들
    public void setProduct(Product product) {
        this.product = product;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }
}
