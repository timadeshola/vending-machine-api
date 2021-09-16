package com.timadeshola.vendingmachine.service.impl;

import com.timadeshola.vendingmachine.service.ApplicationDao;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;

/**
 * Project title: vending-machine
 *
 * @author johnadeshola
 * Date: 9/16/21
 * Time: 5:23 PM
 */
@Repository
public class ApplicationDaoImpl implements ApplicationDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public <T> Long totalCount(CriteriaBuilder builder, Predicate predicate, Class<T> typeClazz) {
        CriteriaQuery<Long> query = builder.createQuery(Long.class);

        query.select(builder.count(query.from(typeClazz)));
        if (predicate != null) {
            query.where(predicate);
        }
        return entityManager.createQuery(query).getSingleResult();
    }
}