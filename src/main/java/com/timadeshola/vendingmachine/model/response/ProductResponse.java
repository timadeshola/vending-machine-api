package com.timadeshola.vendingmachine.model.response;

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
public class ProductResponse implements Serializable {

    private Long id;
    private String name;
    private BigDecimal amountAvailable;
    private BigDecimal cost;
    private UserResponse seller;
    private String dateCreated;
    private String createdBy;
}