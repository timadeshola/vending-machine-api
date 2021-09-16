package com.timadeshola.vendingmachine.service;

import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;

/**
 * Project title: vending-machine
 *
 * @author johnadeshola
 * Date: 9/16/21
 * Time: 5:23 PM
 */
public interface ApplicationDao {

    @Transactional(readOnly = true)
    public <T> Long totalCount(CriteriaBuilder builder, Predicate predicate, Class<T> typeClazz);
}