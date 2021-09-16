package com.timadeshola.vendingmachine.persistence.entity;

import com.timadeshola.vendingmachine.model.enums.RoleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Timestamp;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Project title: vending-machine
 *
 * @author johnadeshola
 * Date: 9/16/21
 * Time: 3:12 PM
 */
class ProductTest {

    private Product product;

    @BeforeEach
    public void setUp() {
        product = Product.builder().build();
    }

    @Test
    @DisplayName("Product Entity Test")
    void testProductEntityObject() {
        product = Product.builder()
                .id(1L)
                .name("Java Expert")
                .amountAvailable(BigDecimal.valueOf(20))
                .cost(BigDecimal.valueOf(20))
                .seller(User.builder()
                        .id(1L)
                        .username("timadeshola")
                        .password("password@123")
                        .deposit(BigDecimal.valueOf(10))
                        .role(RoleType.BUYER)
                        .createdBy("sam.wise")
                        .dateCreated(new Timestamp(System.currentTimeMillis()))
                        .updatedBy("timadeshola")
                        .dateUpdated(new Timestamp(System.currentTimeMillis()))
                        .version(1L)
                        .build())
                .dateCreated(new Timestamp(System.currentTimeMillis()))
                .createdBy("timadeshola")
                .updatedBy("timadeshola")
                .dateUpdated(new Timestamp(System.currentTimeMillis()))
                .version(1L)
                .build();

        assertNotNull(product);
        assertThat(product.getAmountAvailable()).isEqualTo(BigDecimal.valueOf(20));
        assertThat(product).isExactlyInstanceOf(Product.class);
        assertThat(product.getSeller()).isExactlyInstanceOf(User.class);
    }

}