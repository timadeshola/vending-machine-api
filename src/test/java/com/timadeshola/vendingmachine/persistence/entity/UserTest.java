package com.timadeshola.vendingmachine.persistence.entity;

import com.timadeshola.vendingmachine.model.enums.RoleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Timestamp;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Project title: vending-machine
 *
 * @author johnadeshola
 * Date: 9/16/21
 * Time: 3:12 PM
 */
class UserTest {

    private User user;

    @BeforeEach
    public void setUp() {
        user = User.builder().build();
    }

    @Test
    @DisplayName("User Entity Test")
    void testUserEntityObject() {
        user = User.builder()
                .id(1L)
                .username("timadeshola")
                .password("password@123")
                .deposit(BigDecimal.valueOf(10))
                .role(RoleType.BUYER.getRole())
                .createdBy("sam.wise")
                .dateCreated(new Timestamp(System.currentTimeMillis()))
                .updatedBy("timadeshola")
                .dateUpdated(new Timestamp(System.currentTimeMillis()))
                .version(1L)
                .build();

        assertNotNull(user);
        assertThat(user.getUsername()).isEqualTo("timadeshola");
        assertThat(user.getPassword()).isNotEmpty();
        assertEquals(1L, user.getId());
        assertThat(user).isExactlyInstanceOf(User.class);
        assertThat(user.getRole()).isExactlyInstanceOf(RoleType.class);
    }

}