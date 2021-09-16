package com.timadeshola.vendingmachine.persistence.repository;

import com.timadeshola.vendingmachine.model.enums.RoleType;
import com.timadeshola.vendingmachine.persistence.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Project title: vending-machine
 *
 * @author johnadeshola
 * Date: 9/16/21
 * Time: 3:12 PM
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.AUTO_CONFIGURED)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    public void setUp() {
        user = User.builder().build();
    }

    @Test
    @DisplayName("User Repository Test")
    void testUserEntityObject() {

        user = userRepository.save(User.builder()
                .username("timadeshola")
                .password("password@123")
                .deposit(BigDecimal.valueOf(10))
                .role(RoleType.BUYER)
                .build());

        User fetchUser = userRepository.findByUsername(this.user.getUsername()).orElse(null);

        List<User> users = userRepository.findAll();

        assertThat(user).isIn(users);
        assertThat(users).isNotEmpty();
        assertNotNull(user);
        assertNotNull(fetchUser);
        assertThat(user.getRole()).isEqualTo(RoleType.BUYER);
        assertThat(user).isExactlyInstanceOf(User.class);
        assertThat(user.getRole()).isExactlyInstanceOf(RoleType.class);
    }
}