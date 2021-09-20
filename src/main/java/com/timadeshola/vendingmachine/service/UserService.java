package com.timadeshola.vendingmachine.service;

import com.timadeshola.vendingmachine.model.request.UserRequest;
import com.timadeshola.vendingmachine.model.response.PaginateResponse;
import com.timadeshola.vendingmachine.model.response.UserResponse;

import java.math.BigDecimal;

/**
 * Project title: vending-machine
 *
 * @author johnadeshola
 * Date: 9/16/21
 * Time: 5:07 PM
 */
public interface UserService {

    public UserResponse createUser(UserRequest request);

    public UserResponse updateUser(UserRequest request, Long id);

    public Boolean deleteUser(Long id);

    public UserResponse fetchUser(Long id);

    public PaginateResponse<UserResponse> fetchUser(int start,
                                                    int limit,
                                                    String startDate,
                                                    String endDate,
                                                    String username,
                                                    String role);

    public BigDecimal deposit(BigDecimal amount);

    public BigDecimal resetDeposit();
}