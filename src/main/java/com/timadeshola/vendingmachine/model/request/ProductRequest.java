package com.timadeshola.vendingmachine.model.request;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Project title: vending-machine
 *
 * @author johnadeshola
 * Date: 9/16/21
 * Time: 3:32 PM
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ProductRequest implements Serializable {

    private String name;
    private BigDecimal amountAvailable;
    private BigDecimal cost;
    private Long seller;
}