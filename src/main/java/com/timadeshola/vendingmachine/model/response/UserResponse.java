package com.timadeshola.vendingmachine.model.response;

import com.timadeshola.vendingmachine.model.enums.RoleType;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Project title: vending-machine
 *
 * @author johnadeshola
 * Date: 9/16/21
 * Time: 3:29 PM
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserResponse implements Serializable {

    private Long id;
    private String username;
    private String password;
    private BigDecimal deposit;
    private RoleType role;
    private String dateCreated;
    private String createdBy;
}