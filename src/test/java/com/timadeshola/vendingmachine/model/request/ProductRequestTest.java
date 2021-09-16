package com.timadeshola.vendingmachine.model.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Project title: vending-machine
 *
 * @author johnadeshola
 * Date: 9/16/21
 * Time: 3:32 PM
 */
class ProductRequestTest {

    private ProductRequest product;

    @BeforeEach
    public void setUp() {
        product = ProductRequest.builder().build();
    }

    @Test
    @DisplayName("Product Request Test")
    void testProductRequestObject() {
        product = ProductRequest.builder()
                .name("Java Expert")
                .amountAvailable(BigDecimal.valueOf(20))
                .cost(BigDecimal.valueOf(20))
                .seller(1L)
                .build();

        assertNotNull(product);
        assertThat(product.getAmountAvailable()).isEqualTo(BigDecimal.valueOf(20));
        assertThat(product).isExactlyInstanceOf(ProductRequest.class);
    }
}