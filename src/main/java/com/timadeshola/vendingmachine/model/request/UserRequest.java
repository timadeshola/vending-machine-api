package com.timadeshola.vendingmachine.model.request;

import com.timadeshola.vendingmachine.model.enums.RoleType;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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

    @NotBlank(message = "Username cannot be empty")
    @Size(message = "Username cannot be less than 8 and greater than 60 characters", min = 8, max = 60)
    private String username;
    @NotBlank(message = "Password cannot be blank")
    private String password;
    @NotNull(message = "Select role for the user")
    private RoleType role;
}