package com.timadeshola.vendingmachine.core.exceptions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Project title: vending-machine
 *
 * @author johnadeshola
 * Date: 9/16/21
 * Time: 12:58 PM
 */
class CustomExceptionTest {

    @DisplayName("Custom Exception Test")
    @Test
    void testCustomException() {
        Throwable exception = assertThrows(CustomException.class, () -> {
            throw new CustomException("Not supported", HttpStatus.CONFLICT);
        });
        assertEquals("Not supported", exception.getMessage());
    }
}