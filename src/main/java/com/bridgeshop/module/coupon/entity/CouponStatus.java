package com.bridgeshop.module.coupon.entity;

public enum CouponStatus {
    NEW,                // 발급 대기
    ACTIVE,             // 사용 가능
    USED,               // 사용 완료
    EXPIRED,            // 유효 기간 만료
    DEACTIVATED         // 사용 중지
}
