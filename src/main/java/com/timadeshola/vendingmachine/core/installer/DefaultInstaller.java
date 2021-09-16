package com.timadeshola.vendingmachine.core.installer;

import com.timadeshola.vendingmachine.model.enums.RoleType;
import com.timadeshola.vendingmachine.persistence.entity.CustomClientDetails;
import com.timadeshola.vendingmachine.persistence.entity.User;
import com.timadeshola.vendingmachine.persistence.repository.CustomClientDetailRepository;
import com.timadeshola.vendingmachine.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Optional;

/**
 * Project title: vending-machine
 *
 * @author johnadeshola
 * Date: 9/16/21
 * Time: 11:19 PM
 */
@Service
@RequiredArgsConstructor
public class DefaultInstaller implements ApplicationListener<ContextRefreshedEvent> {

    private final UserRepository userRepository;
    ;
    private final CustomClientDetailRepository clientDetailRepository;
    private final PasswordEncoder passwordEncoder;
    private boolean alreadySetup = false;

    @Value("${spring.application.name}")
    private String appName;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (alreadySetup) {
            return;
        }

        createUserIfNotExist("password", "timadeshola", RoleType.BUYER);
        createUserIfNotExist("password", "ennyadeshola", RoleType.SELLER);
        createCustomClientDetailsIfNotExist(appName);

        alreadySetup = true;
    }

    private User createUserIfNotExist(String password, String username, RoleType role) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }
        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .role(role)
                .createdBy("system")
                .dateCreated(new Timestamp(System.currentTimeMillis()))
                .build();
        user = userRepository.save(user);
        return user;
    }

    private void createCustomClientDetailsIfNotExist(String clientId) {
        Optional<CustomClientDetails> clientDetailsOptional = clientDetailRepository.findByClientId(clientId);
        if (clientDetailsOptional.isPresent()) {
            return;
        }
        CustomClientDetails clientDetails = new CustomClientDetails();
        clientDetails.setClientId(clientId);
        clientDetails.setClientSecret(passwordEncoder.encode("password"));
        clientDetails.setResourceIds(new HashSet<>(Collections.singletonList(clientId.toUpperCase(Locale.ENGLISH))));
        clientDetails.setScope(new HashSet<>(Collections.singletonList("read,write,trust")));
        clientDetails.setAuthorizedGrantTypes(new HashSet<>(Collections.singletonList("authorization_code,password,refresh_token,implicit,mobile,client_credentials")));
        clientDetails.setWebServerRedirectUri(null);
        clientDetails.setAuthorities(null);
        clientDetails.setAccessTokenValiditySeconds(21600); //6hrs
        clientDetails.setRefreshTokenValiditySeconds(43200); //12hrs
        clientDetails.setAdditionalInformation(null);
        clientDetails.setCreatedBy("system");
        clientDetails.setDateCreated(new Timestamp(System.currentTimeMillis()));
        clientDetailRepository.save(clientDetails);
    }
}