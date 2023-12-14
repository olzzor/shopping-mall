package com.bridgeshop.module.contact.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ContactListResponse {
    private List<ContactDto> contacts;
    private int totalPages;

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public ContactListResponse(List<ContactDto> contacts, int totalPages) {
        this.contacts = contacts;
        this.totalPages = totalPages;
    }
}
