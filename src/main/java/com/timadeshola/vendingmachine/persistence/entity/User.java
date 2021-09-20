package com.timadeshola.vendingmachine.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
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
    @Builder.Default
    private BigDecimal deposit = BigDecimal.valueOf(0.0);

    @Column(name = "role", length = 10)
    private String role;

}