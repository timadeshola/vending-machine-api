package com.timadeshola.vendingmachine.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

/**
 * Project title: vending-machine
 *
 * @author johnadeshola
 * Date: 9/16/21
 * Time: 5:29 PM
 */
@Configuration
public class PasswordEncoderConfig {

    private static final int STRENGTH = 10;
    private static final int ITERATION = 200_000;
    private static final int HASH = 1024;
    @Value("${app.secret.hash}")
    private String secret;

    public static PasswordEncoder createDelegatingPasswordEncoder() {
        String encodingId = "argon2";
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put("bcrypt", new BCryptPasswordEncoder());
        encoders.put("pbkdf2", new Pbkdf2PasswordEncoder());
        encoders.put("scrypt", new SCryptPasswordEncoder());
        encoders.put("argon2", new Argon2PasswordEncoder());

        DelegatingPasswordEncoder delegatingPasswordEncoder = new DelegatingPasswordEncoder(encodingId, encoders);
        delegatingPasswordEncoder.setDefaultPasswordEncoderForMatches(new Argon2PasswordEncoder());
        return delegatingPasswordEncoder;
    }

    @Bean
    @Primary
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderConfig.createDelegatingPasswordEncoder();
    }

    @Bean(name = "bcrypt")
    public PasswordEncoder bcryptPasswordEncoder() {
        return new BCryptPasswordEncoder(STRENGTH, new SecureRandom());
    }

    @Bean(name = "argon2")
    public PasswordEncoder argon2PasswordEncoder() {
        return new Argon2PasswordEncoder();
    }

    @Bean(name = "scrypt")
    public PasswordEncoder scryptPasswordEncoder() {
        return new SCryptPasswordEncoder();
    }

    @Bean(name = "pbkdf2")
    public PasswordEncoder pbkdf2PasswordEncoder() {
        return new Pbkdf2PasswordEncoder(secret, ITERATION, HASH);
    }

}