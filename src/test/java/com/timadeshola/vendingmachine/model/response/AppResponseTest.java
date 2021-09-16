package com.timadeshola.vendingmachine.model.response;

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
 * Time: 1:01 PM
 */
class AppResponseTest {

    private AppResponse<ProductResponse> response;

    @BeforeEach
    public void setUp() {
        response = AppResponse.<ProductResponse>builder().build();
    }

    @Test
    @DisplayName("Role Response Test")
    void testApiResponseObject() {
        response = AppResponse.<ProductResponse>builder()
                .data(ProductResponse.builder()
                        .id(1L)
                        .name("BUYER")
                        .amountAvailable(BigDecimal.valueOf(10))
                        .cost(BigDecimal.valueOf(10))
                        .createdBy("joe.bruce")
                        .dateCreated(String.valueOf(new Timestamp(System.currentTimeMillis())))
                        .build())
                .message("BUYER")
                .status(200)
                .error("joe.bruce")
                .build();

        assertNotNull(response);
        assertThat(response.getData()).isNotNull();
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response).isExactlyInstanceOf(AppResponse.class);
        assertThat(response.getData()).isExactlyInstanceOf(ProductResponse.class);
    }
}