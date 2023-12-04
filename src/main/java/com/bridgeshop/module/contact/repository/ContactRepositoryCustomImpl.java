package com.bridgeshop.module.contact.repository;

import com.bridgeshop.module.contact.entity.Contact;
import com.bridgeshop.module.contact.dto.ContactListSearchRequest;
import com.bridgeshop.module.contact.entity.QContact;
import com.bridgeshop.common.util.QueryDslUtils;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;


public class ContactRepositoryCustomImpl implements ContactRepositoryCustom {
    @PersistenceContext
    private EntityManager em;

    @Override
    public Page<Contact> findByCondition(ContactListSearchRequest contactListSearchRequest, Pageable pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QContact qContact = QContact.contact;

        JPAQuery<Contact> query = queryFactory
                .selectFrom(qContact)
                .where(
                        qContact.step.eq(0),
                        QueryDslUtils.likeString(qContact.title, contactListSearchRequest.getTitle()),
                        QueryDslUtils.likeString(qContact.inquirerName, contactListSearchRequest.getInquirerName()),
                        QueryDslUtils.likeString(qContact.inquirerEmail, contactListSearchRequest.getInquirerEmail()),
                        QueryDslUtils.likeString(qContact.orderNumber, contactListSearchRequest.getOrderNumber()),
                        QueryDslUtils.eqContactType(qContact.type, contactListSearchRequest.getType()),
                        QueryDslUtils.eqContactStatus(qContact.status, contactListSearchRequest.getStatus()),
                        QueryDslUtils.betweenDate(qContact.regDate, contactListSearchRequest.getStartRegDate(), contactListSearchRequest.getEndRegDate())
                )
                .orderBy(qContact.regDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        List<Contact> contacts = query.fetch();
        long totalCount = query.fetchCount();

        return new PageImpl<>(contacts, pageable, totalCount);
    }
}