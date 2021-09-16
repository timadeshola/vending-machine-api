package com.timadeshola.vendingmachine.model.response;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * Project title: vending-machine
 *
 * @author johnadeshola
 * Date: 9/16/21
 * Time: 5:12 PM
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PaginateResponse<T> implements Serializable {

    private List<T> content;
    private long totalElements;
}