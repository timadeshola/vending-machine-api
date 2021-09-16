package com.timadeshola.vendingmachine.model.response;

import lombok.*;

import java.io.Serializable;

/**
 * Project title: vending-machine
 *
 * @author johnadeshola
 * Date: 9/16/21
 * Time: 1:01 PM
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AppResponse<T> implements Serializable {

    private T data;
    private String message;
    private int status;
    private Object error;
}