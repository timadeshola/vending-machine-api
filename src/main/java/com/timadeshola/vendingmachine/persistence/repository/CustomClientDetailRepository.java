package com.timadeshola.vendingmachine.persistence.repository;

import com.timadeshola.vendingmachine.persistence.entity.CustomClientDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Project title: vending-machine
 *
 * @author johnadeshola
 * Date: 9/16/21
 * Time: 12:23 PM
 */
public interface CustomClientDetailRepository extends JpaRepository<CustomClientDetails, Long> {

    Optional<CustomClientDetails> findByClientId(String clientId);
}