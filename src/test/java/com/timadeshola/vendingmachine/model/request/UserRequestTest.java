package com.timadeshola.vendingmachine.model.request;

import com.timadeshola.vendingmachine.model.enums.RoleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
class UserRequestTest {

    private UserRequest user;

    @BeforeEach
    public void setUp() {
        user = UserRequest.builder().build();
    }

    @Test
    @DisplayName("User Request Test")
    void testUserRequestObject() {
        user = UserRequest.builder()
                .username("timadeshola")
                .password("password@123")
                .role(RoleType.BUYER)
                .build();

        assertNotNull(user);
        assertThat(user.getUsername()).isEqualTo("timadeshola");
        assertThat(user.getPassword()).isNotEmpty();
        assertEquals(RoleType.BUYER, user.getRole());
        assertThat(user).isExactlyInstanceOf(UserRequest.class);
    }
}