package com.timadeshola.vendingmachine.service;

import com.timadeshola.vendingmachine.model.enums.RoleType;
import com.timadeshola.vendingmachine.model.request.ProductRequest;
import com.timadeshola.vendingmachine.model.response.PaginateResponse;
import com.timadeshola.vendingmachine.model.response.ProductResponse;
import com.timadeshola.vendingmachine.model.response.UserResponse;
import com.timadeshola.vendingmachine.persistence.entity.Product;
import com.timadeshola.vendingmachine.persistence.entity.User;
import com.timadeshola.vendingmachine.persistence.repository.ProductRepository;
import com.timadeshola.vendingmachine.persistence.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.sql.Timestamp;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Project title: vending-machine
 *
 * @author johnadeshola
 * Date: 9/16/21
 * Time: 5:09 PM
 */
@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    private ProductResponse product;
    private ProductRequest request;

    @BeforeEach
    public void setUp() {
        product = ProductResponse.builder().build();
        request = ProductRequest.builder().build();
    }

    @DisplayName("Create Product Service Test")
    @Test
    void testCreateProduct() {
        User user = createUser();
        request = ProductRequest.builder()
                .name("Jave Expert")
                .amountAvailable(BigDecimal.valueOf(20))
                .cost(BigDecimal.valueOf(20))
                .seller(user.getId())
                .build();

        product = productService.createProduct(request);

        assertNotNull(user);
        assertNotNull(product);
        assertThat(product.getSeller().getUsername()).isEqualTo(user.getUsername());
        assertThat(product.getAmountAvailable()).isEqualTo(BigDecimal.valueOf(20));
        assertThat(product).isExactlyInstanceOf(ProductResponse.class);
        assertThat(product.getSeller()).isExactlyInstanceOf(UserResponse.class);
    }

    @DisplayName("Update Product Service Test")
    @Test
    void testUpdateProduct() {
        Product existingProduct = createProduct();
        request = ProductRequest.builder()
                .name("Java Expert Info")
                .amountAvailable(BigDecimal.valueOf(20))
                .cost(BigDecimal.valueOf(20))
                .seller(existingProduct.getSeller().getId())
                .build();
        this.product = productService.updateProduct(request, existingProduct.getId());

        assertNotNull(existingProduct);
        assertNotNull(request);
        assertNotNull(product);
        assertThat(request.getName()).isEqualTo(product.getName());
        assertThat(product.getSeller().getUsername()).isEqualTo(existingProduct.getSeller().getUsername());
        assertThat(product.getAmountAvailable()).isEqualTo(BigDecimal.valueOf(20));
        assertThat(product).isExactlyInstanceOf(ProductResponse.class);
        assertThat(product.getSeller()).isExactlyInstanceOf(UserResponse.class);
    }

    @DisplayName("Delete Product Service Test")
    @Test
    void testDeleteProduct() {
        Product existingProduct = createProduct();
        Boolean resp = productService.deleteProduct(1L);
        Product fetchProduct = productRepository.findById(existingProduct.getId()).orElse(null);
        assertNotNull(existingProduct);
        assertNotNull(resp);
        assertNull(fetchProduct);
        assertTrue(resp);
    }

    @DisplayName("Fetch Product By ID Service Test")
    @Test
    void testFetchProduct() {
        Product existingProduct = createProduct();
        this.product = productService.fetchProduct(1L);

        assertNotNull(existingProduct);
        assertNotNull(product);
        assertThat(product.getName()).isEqualTo(existingProduct.getName());
        assertThat(product.getSeller().getUsername()).isEqualTo(existingProduct.getSeller().getUsername());
        assertThat(product).isExactlyInstanceOf(ProductResponse.class);
        assertThat(product.getSeller()).isExactlyInstanceOf(UserResponse.class);
    }

    @DisplayName("Fetch Products Service Test")
    @Test
    void testFetchProducts() {
        Product existingProduct = createProduct();
        PaginateResponse<ProductResponse> products = productService.fetchProducts(0, 10, null, null, null);
        assertNotNull(existingProduct);
        assertNotNull(products);
        assertTrue(products.getContent().size() > 0);
        assertThat(products).isExactlyInstanceOf(PaginateResponse.class);
    }

    User createUser() {

        return userRepository.save(User.builder()
                .username("timadeshola")
                .password("password@123")
                .deposit(BigDecimal.valueOf(10))
                .role(RoleType.BUYER)
                .build());
    }

    Product createProduct() {

        User user = userRepository.save(User.builder()
                .username("timadeshola")
                .password("password@123")
                .deposit(BigDecimal.valueOf(10))
                .role(RoleType.BUYER)
                .build());

        Product product = Product.builder()
                .name("Java Expert")
                .amountAvailable(BigDecimal.valueOf(20.0))
                .cost(BigDecimal.valueOf(20.0))
                .seller(user)
                .createdBy("sam.wise")
                .dateCreated(new Timestamp(System.currentTimeMillis()))
                .updatedBy("timadeshola")
                .dateUpdated(new Timestamp(System.currentTimeMillis()))
                .dateCreated(new Timestamp(System.currentTimeMillis()))
                .createdBy("timadeshola")
                .updatedBy("timadeshola")
                .dateUpdated(new Timestamp(System.currentTimeMillis()))
                .build();

        return productRepository.save(product);
    }
}