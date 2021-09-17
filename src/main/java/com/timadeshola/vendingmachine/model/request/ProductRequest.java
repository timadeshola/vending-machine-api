package com.timadeshola.vendingmachine.model.request;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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

    @NotBlank(message = "Product name cannot be empty")
    private String name;
    @NotNull(message = "Product cost cannot be empty")
    private BigDecimal cost;
}