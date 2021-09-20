package com.timadeshola.vendingmachine.resource;

import com.timadeshola.vendingmachine.core.AppConstant;
import com.timadeshola.vendingmachine.model.request.ProductRequest;
import com.timadeshola.vendingmachine.model.response.AppResponse;
import com.timadeshola.vendingmachine.model.response.PaginateResponse;
import com.timadeshola.vendingmachine.model.response.ProductPurchaseResponse;
import com.timadeshola.vendingmachine.model.response.ProductResponse;
import com.timadeshola.vendingmachine.service.ProductService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Project title: vending-machine
 *
 * @author johnadeshola
 * Date: 9/16/21
 * Time: 9:56 PM
 */
@RestController
@RequestMapping("products")
@RequiredArgsConstructor
@Api(tags = "Product Management API")
public class ProductResource {

    private final ProductService productService;

    @PostMapping
    @PreAuthorize("hasAuthority('SELLER')")
    public ResponseEntity<AppResponse<ProductResponse>> createProduct(@RequestBody @Valid ProductRequest request) {
        ProductResponse response = productService.createProduct(request);
        return ResponseEntity.ok().body(AppResponse.<ProductResponse>builder()
                .message(AppConstant.RestMessage.success)
                .status(HttpStatus.CREATED.value())
                .data(response)
                .build());
    }

    @PutMapping
    @PreAuthorize("hasAuthority('SELLER')")
    public ResponseEntity<AppResponse<ProductResponse>> updateProduct(@RequestBody @Valid ProductRequest request, @RequestParam Long id) {
        ProductResponse response = productService.updateProduct(request, id);
        return ResponseEntity.ok().body(AppResponse.<ProductResponse>builder()
                .message(AppConstant.RestMessage.success)
                .status(HttpStatus.OK.value())
                .data(response)
                .build());

    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('SELLER')")
    public ResponseEntity<AppResponse<Boolean>> deleteProduct(@RequestParam Long id) {
        Boolean response = productService.deleteProduct(id);
        return ResponseEntity.ok().body(AppResponse.<Boolean>builder()
                .message(AppConstant.RestMessage.success)
                .status(HttpStatus.OK.value())
                .data(response)
                .build());

    }

    @GetMapping("viewProduct/{id:[\\d]}")
    public ResponseEntity<AppResponse<ProductResponse>> fetchProduct(@PathVariable Long id) {
        ProductResponse response = productService.fetchProduct(id);
        return ResponseEntity.ok().body(AppResponse.<ProductResponse>builder()
                .message(AppConstant.RestMessage.success)
                .status(HttpStatus.OK.value())
                .data(response)
                .build());

    }

    @GetMapping("viewProducts")
    public ResponseEntity<AppResponse<PaginateResponse<ProductResponse>>> fetchProducts(@RequestParam(defaultValue = "0") int start,
                                                                                        @RequestParam(defaultValue = "10") int limit,
                                                                                        @RequestParam(required = false) String startDate,
                                                                                        @RequestParam(required = false) String endDate,
                                                                                        @RequestParam(required = false) String sellerName) {
        PaginateResponse<ProductResponse> response = productService.fetchProducts(start, limit, startDate, endDate, sellerName);
        return ResponseEntity.ok().body(AppResponse.<PaginateResponse<ProductResponse>>builder()
                .message(AppConstant.RestMessage.success)
                .status(HttpStatus.OK.value())
                .data(response)
                .build());

    }

    @PostMapping("buy/{id:[\\d]}")
    @PreAuthorize("hasAuthority('BUYER')")
    public ResponseEntity<AppResponse<ProductPurchaseResponse>> purchaseProduct(@PathVariable Long id) {
        ProductPurchaseResponse response = productService.purchaseProduct(id);
        return ResponseEntity.ok().body(AppResponse.<ProductPurchaseResponse>builder()
                .message(AppConstant.RestMessage.success)
                .status(HttpStatus.OK.value())
                .data(response)
                .build());
    }

}