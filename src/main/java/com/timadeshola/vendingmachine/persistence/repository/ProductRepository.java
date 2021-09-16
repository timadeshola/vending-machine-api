package com.timadeshola.vendingmachine.persistence.repository;

import com.timadeshola.vendingmachine.persistence.entity.Product;
import com.timadeshola.vendingmachine.persistence.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Project title: vending-machine
 *
 * @author johnadeshola
 * Date: 9/16/21
 * Time: 3:13 PM
 */
public interface ProductRepository extends JpaRepository<Product, Long> {

    public List<Product> findAllBySeller(User user);

    public Optional<Product> findByNameAndSeller_Id(String name, Long id);

    Optional<Product> findByIdAndSeller_Id(Long name, Long seller);
}