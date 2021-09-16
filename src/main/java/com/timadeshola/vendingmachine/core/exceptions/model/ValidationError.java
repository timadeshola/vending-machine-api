package com.timadeshola.vendingmachine.core.exceptions.model;

import lombok.*;

import java.io.Serializable;

/**
 * Project title: vending-machine
 *
 * @author johnadeshola
 * Date: 9/16/21
 * Time: 12:59 PM
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ValidationError implements Serializable {

    private Object rejectedValue;
    private String field;
    private String objectName;
}