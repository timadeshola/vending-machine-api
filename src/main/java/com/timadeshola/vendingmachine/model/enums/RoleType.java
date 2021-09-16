package com.timadeshola.vendingmachine.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Project title: vending-machine
 *
 * @author johnadeshola
 * Date: 9/16/21
 * Time: 9:18 PM
 */
@Getter
@AllArgsConstructor
public enum RoleType {

    BUYER("BUYER"),
    SELLER("SELLER");

    private String role;
}