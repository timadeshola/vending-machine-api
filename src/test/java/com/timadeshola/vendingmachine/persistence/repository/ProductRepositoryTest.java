package com.timadeshola.vendingmachine.persistence.repository;

import com.timadeshola.vendingmachine.model.enums.RoleType;
import com.timadeshola.vendingmachine.persistence.entity.Product;
import com.timadeshola.vendingmachine.persistence.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Project title: vending-machine
 *
 * @author johnadeshola
 * Date: 9/16/21
 * Time: 3:13 PM
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.AUTO_CONFIGURED)
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    private Product product;
    private User user;

    @BeforeEach
    public void setUp() {
        product = Product.builder().build();
        user = User.builder().build();
    }

    @Test
    @DisplayName("Product Repository Test")
    void testProductEntityObject() {

        user = userRepository.save(User.builder()
                .username("timadeshola")
                .password("password@123")
                .deposit(BigDecimal.valueOf(10))
                .role(RoleType.BUYER.getRole())
                .build());

        product = Product.builder()
                .name("Java Expert")
                .amountAvailable(BigDecimal.valueOf(20))
                .cost(BigDecimal.valueOf(20))
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

        Product saveProduct = productRepository.save(product);

        List<Product> products = productRepository.findAllBySeller(user);

        assertTrue(products.size() > 0);
        assertThat(product).isIn(products);
        assertThat(products).isNotEmpty();
        assertNotNull(product);
        assertThat(product.getSeller()).isEqualTo(user);
        assertThat(user.getRole()).isEqualTo(RoleType.BUYER);
        assertThat(product.getAmountAvailable()).isEqualTo(saveProduct.getAmountAvailable());
        assertThat(product.getAmountAvailable()).isEqualTo(BigDecimal.valueOf(20));
        assertThat(product).isExactlyInstanceOf(Product.class);
        assertThat(product.getSeller()).isExactlyInstanceOf(User.class);
    }
}