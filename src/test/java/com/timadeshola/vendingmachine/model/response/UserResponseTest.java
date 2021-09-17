package com.timadeshola.vendingmachine.model.response;

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
 * Time: 3:29 PM
 */
class UserResponseTest {


    private UserResponse user;

    @BeforeEach
    public void setUp() {
        user = UserResponse.builder().build();
    }

    @Test
    @DisplayName("User Response Test")
    void testUserResponseObject() {
        user = UserResponse.builder()
                .id(1L)
                .username("timadeshola")
                .deposit(BigDecimal.valueOf(10))
                .role(RoleType.BUYER)
                .createdBy("sam.wise")
                .dateCreated(String.valueOf(new Timestamp(System.currentTimeMillis())))
                .build();

        assertNotNull(user);
        assertThat(user.getUsername()).isEqualTo("timadeshola");
        assertEquals(1L, user.getId());
        assertThat(user).isExactlyInstanceOf(UserResponse.class);
        assertThat(user.getRole()).isExactlyInstanceOf(RoleType.class);
    }
}