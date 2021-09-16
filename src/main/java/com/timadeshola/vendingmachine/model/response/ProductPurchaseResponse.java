package com.timadeshola.vendingmachine.model.response;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Project title: vending-machine
 *
 * @author johnadeshola
 * Date: 9/16/21
 * Time: 9:43 PM
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ProductPurchaseResponse implements Serializable {

    private BigDecimal totalExpense;
    private String product;
    private BigDecimal balance;
}