package com.timadeshola.vendingmachine.service;

import com.timadeshola.vendingmachine.model.request.ProductRequest;
import com.timadeshola.vendingmachine.model.response.PaginateResponse;
import com.timadeshola.vendingmachine.model.response.ProductPurchaseResponse;
import com.timadeshola.vendingmachine.model.response.ProductResponse;

/**
 * Project title: vending-machine
 *
 * @author johnadeshola
 * Date: 9/16/21
 * Time: 5:09 PM
 */
public interface ProductService {

    public ProductResponse createProduct(ProductRequest request);

    public ProductResponse updateProduct(ProductRequest request, Long id);

    public Boolean deleteProduct(Long id);

    public ProductResponse fetchProduct(Long id);

    public PaginateResponse<ProductResponse> fetchProducts(int start,
                                                           int limit,
                                                           String startDate,
                                                           String endDate,
                                                           String sellerName);

    public ProductPurchaseResponse purchaseProduct(Long id);
}