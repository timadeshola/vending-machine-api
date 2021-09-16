package com.timadeshola.vendingmachine.service;

import com.timadeshola.vendingmachine.persistence.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Project title: vending-machine
 *
 * @author johnadeshola
 * Date: 9/16/21
 * Time: 5:23 PM
 */
@SpringBootTest
class ApplicationDaoTest {

    @Autowired
    private ApplicationDao applicationDao;

    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("Entity Total Count Test")
    void testTotalCount() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        List<Predicate> predicates = new LinkedList<>();
        Predicate predicate = builder.and(predicates.toArray(new Predicate[]{}));
        Long totalCount = applicationDao.totalCount(builder, predicate, User.class);
        assertNotNull(totalCount);
        assertThat(totalCount).isEqualTo(1);
    }


}