package com.timadeshola.vendingmachine.persistence.entity;

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
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Product extends BaseEntity {

    @Column(name = "product_name", length = 100)
    private String name;

    @Column(name = "amount_available", length = 19)
    private BigDecimal amountAvailable;

    @Column(name = "cost", length = 19)
    private BigDecimal cost;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private User seller;

}