package com.timadeshola.vendingmachine.service;

import com.timadeshola.vendingmachine.model.enums.RoleType;
import com.timadeshola.vendingmachine.model.request.UserRequest;
import com.timadeshola.vendingmachine.model.response.PaginateResponse;
import com.timadeshola.vendingmachine.model.response.UserResponse;
import com.timadeshola.vendingmachine.persistence.entity.User;
import com.timadeshola.vendingmachine.persistence.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Project title: vending-machine
 *
 * @author johnadeshola
 * Date: 9/16/21
 * Time: 5:07 PM
 */
@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private UserResponse user;
    private UserRequest request;

    @BeforeEach
    public void setUp() {
        user = UserResponse.builder().build();
        request = UserRequest.builder().build();
    }

    @DisplayName("Create User Service Test")
    @Test
    void testCreateUser() {
        request = UserRequest.builder()
                .username("timadeshola")
                .password("password@123")
                .role(RoleType.BUYER)
                .build();

        user = userService.createUser(request);

        assertNotNull(request);
        assertNotNull(user);
        assertThat(user.getUsername()).isEqualTo("timadeshola");
        assertEquals(1L, user.getId());
        assertThat(user).isExactlyInstanceOf(UserResponse.class);
    }

    @DisplayName("Update User Service Test")
    @Test
    void testUpdateUser() {
        User existingUser = createUser();

        request = UserRequest.builder()
                .username("timadeshola")
                .password("password@123")
                .build();

        user = userService.updateUser(request, existingUser.getId());

        assertNotNull(request);
        assertNotNull(user);
        assertThat(user.getUsername()).isEqualTo("timadeshola");
        assertEquals(BigDecimal.valueOf(15.00), user.getDeposit());
        assertEquals(BigDecimal.valueOf(10.00), existingUser.getDeposit());
        assertThat(user).isExactlyInstanceOf(UserResponse.class);
    }

    @DisplayName("Delete User Service Test")
    @Test
    void testDeleteUser() {
        User existingUser = createUser();
        Boolean resp = userService.deleteUser(existingUser.getId());
        User fetchUser = userRepository.findById(existingUser.getId()).orElse(null);

        assertNotNull(existingUser);
        assertNotNull(resp);
        assertNull(fetchUser);
        assertTrue(resp);
    }

    @DisplayName("Fetch User By ID Service Test")
    @Test
    void testFetchUser() {
        User existingUser = createUser();
        user = userService.fetchUser(existingUser.getId());
        assertNotNull(user);
        assertNotNull(existingUser);
        assertThat(existingUser.getUsername()).isEqualTo(user.getUsername());
        assertThat(user).isExactlyInstanceOf(UserResponse.class);
        assertThat(existingUser).isExactlyInstanceOf(User.class);
    }

    @DisplayName("Fetch Users Service Test")
    @Test
    void testFetchUsers() {
        User existingUser = createUser();
        PaginateResponse<UserResponse> users = userService.fetchUser(0, 10, null, null, null, null);
        assertNotNull(existingUser);
        assertNotNull(users);
        assertTrue(users.getContent().size() > 0);
        assertThat(users).isExactlyInstanceOf(PaginateResponse.class);
    }

    @DisplayName("Deposit Service Test")
    @Test
    void deposit() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(RoleType.BUYER.getRole()));
        org.springframework.security.core.userdetails.User user = new org.springframework.security.core.userdetails.User("timadeshola", "password", true, true, true, true, authorities);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), authorities);
        SecurityContextHolder.getContext().setAuthentication(token);
        BigDecimal resp = userService.deposit(BigDecimal.valueOf(10));
        assertNotNull(resp);
        assertThat(resp).isExactlyInstanceOf(BigDecimal.class);
    }

    @DisplayName("Reset Deposit Account Service Test")
    @Test
    void resetDeposit() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(RoleType.BUYER.getRole()));
        org.springframework.security.core.userdetails.User user = new org.springframework.security.core.userdetails.User("timadeshola", "password", true, true, true, true, authorities);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), authorities);
        SecurityContextHolder.getContext().setAuthentication(token);
        BigDecimal resp = userService.resetDeposit();
        assertNotNull(resp);
        assertThat(resp).isEqualTo(BigDecimal.valueOf(0));
        assertThat(resp).isExactlyInstanceOf(BigDecimal.class);
    }


    User createUser() {

        return userRepository.save(User.builder()
                .username("timadeshola")
                .password("password@123")
                .deposit(BigDecimal.valueOf(10))
                .role(RoleType.BUYER.getRole())
                .build());
    }

}