package com.timadeshola.vendingmachine.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.timadeshola.vendingmachine.model.enums.RoleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Project title: vending-machine
 *
 * @author johnadeshola
 * Date: 9/16/21
 * Time: 3:12 PM
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class User extends BaseEntity {

    @Column(name = "username", length = 60)
    private String username;

    @JsonIgnore
    @Column(name = "password", length = 125)
    private String password;

    @Column(name = "deposit", length = 19)
    private BigDecimal deposit;

    @Column(name = "role", length = 10)
    @Enumerated(EnumType.STRING)
    private RoleType role;

}