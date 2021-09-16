package com.timadeshola.vendingmachine.model.response;

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
 * Time: 3:32 PM
 */
class ProductResponseTest {

    private ProductResponse product;

    @BeforeEach
    public void setUp() {
        product = ProductResponse.builder().build();
    }

    @Test
    @DisplayName("Product Response Test")
    void testProductResponseObject() {
        product = ProductResponse.builder()
                .name("Java Expert")
                .amountAvailable(BigDecimal.valueOf(20))
                .cost(BigDecimal.valueOf(20))
                .seller(UserResponse.builder()
                        .id(1L)
                        .username("timadeshola")
                        .password("password@123")
                        .deposit(BigDecimal.valueOf(10))
                        .role(RoleType.SELLER)
                        .createdBy("sam.wise")
                        .dateCreated(String.valueOf(new Timestamp(System.currentTimeMillis())))
                        .build())
                .createdBy("sam.wise")
                .dateCreated(String.valueOf(new Timestamp(System.currentTimeMillis())))
                .build();

        assertNotNull(product);
        assertThat(product.getAmountAvailable()).isEqualTo(BigDecimal.valueOf(20));
        assertNotNull(product.getSeller());
        assertThat(product.getSeller()).isExactlyInstanceOf(UserResponse.class);
    }
}