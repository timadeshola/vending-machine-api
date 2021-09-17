package com.timadeshola.vendingmachine.model.request;

import com.timadeshola.vendingmachine.model.enums.RoleType;
import lombok.*;

import java.io.Serializable;

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
public class UserRequest implements Serializable {

    private String username;
    private String password;
    private RoleType role;
}