package com.timadeshola.vendingmachine.config;

import com.timadeshola.vendingmachine.core.security.CustomClientDetailsConfig;
import com.timadeshola.vendingmachine.core.security.CustomOAuth2RequestFactory;
import com.timadeshola.vendingmachine.core.security.CustomTokenEnhancer;
import com.timadeshola.vendingmachine.core.security.CustomUserDetailsService;
import com.timadeshola.vendingmachine.persistence.repository.UserRepository;
import io.micrometer.core.instrument.util.IOUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpointAuthenticationFilter;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.io.IOException;

/**
 * Project title: vending-machine
 *
 * @author johnadeshola
 * Date: 9/16/21
 * Time: 11:18 PM
 */
@Configuration
@EnableAuthorizationServer
@RequiredArgsConstructor
@Slf4j
public class OAuth2AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    private final AuthenticationManager authenticationManager;
    private final CustomClientDetailsConfig clientDetailsService;
    private final CustomUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Value("${check-user-scopes}")
    private boolean checkUserScopes;

    @Value("${certificate.passcode}")
    private String passcode;

    @Value("${certificate.keystore-alias}")
    private String keystoreAlias;

    private static String getPublicKey() {
        Resource resource = new ClassPathResource("vending-machine-pub.txt");
        String publicKey;
        try {
            publicKey = IOUtils.toString(resource.getInputStream());
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
        return publicKey;
    }

    @Bean
    public OAuth2RequestFactory requestFactory() {
        CustomOAuth2RequestFactory requestFactory = new CustomOAuth2RequestFactory(clientDetailsService, tokenStore(), userRepository);
        requestFactory.setCheckUserScopes(checkUserScopes);
        return requestFactory;
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        final JwtAccessTokenConverter converter = new CustomTokenEnhancer();
        converter.setKeyPair(new KeyStoreKeyFactory(new ClassPathResource("vending-machine-jwt.jks"), passcode.toCharArray()).getKeyPair(keystoreAlias));
        converter.setVerifierKey(getPublicKey());
        return converter;
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetailsService);
    }

    @Bean
    public TokenEndpointAuthenticationFilter tokenEndpointAuthenticationFilter() {
        return new TokenEndpointAuthenticationFilter(authenticationManager, requestFactory());
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security
                .tokenKeyAccess("permitAll")
                .checkTokenAccess("isAuthenticated()")
                .passwordEncoder(passwordEncoder)
                .allowFormAuthenticationForClients();
    }

    @Override
    public void configure(final AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints
                .tokenStore(tokenStore())
                .tokenEnhancer(jwtAccessTokenConverter())
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService)
                .pathMapping("/oauth/token", "/oauth/token")
                .pathMapping("/oauth/authorize", "/oauth/authorize")
                .pathMapping("/oauth/confirm_access", "/oauth/confirm_access")
                .pathMapping("/oauth/check_token", "/oauth/check_token")
                .pathMapping("/oauth/userinfo", "/oauth/userinfo")
                .pathMapping("/oauth/error", "/oauth/error")
                .pathMapping("/error", "/error")
                .pathMapping("/oauth/token_key", "/oauth/token_key")
                .exceptionTranslator(loggingExceptionTranslator());
        if (checkUserScopes) {
            endpoints.requestFactory(requestFactory());
        }
    }

    @Bean
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setTokenStore(tokenStore());
        tokenServices.setClientDetailsService(clientDetailsService);
        tokenServices.setAuthenticationManager(authenticationManager);
        return tokenServices;
    }

    @Bean
    public WebResponseExceptionTranslator<OAuth2Exception> loggingExceptionTranslator() {
        return new DefaultWebResponseExceptionTranslator() {
            @Override
            public ResponseEntity<OAuth2Exception> translate(Exception e) throws Exception {
                log.error("error translating http response:: {}", e.getMessage());
                ResponseEntity<OAuth2Exception> responseEntity = super.translate(e);
                HttpHeaders headers = new HttpHeaders();
                headers.setAll(responseEntity.getHeaders().toSingleValueMap());
                OAuth2Exception excBody = responseEntity.getBody();
                return new ResponseEntity<>(excBody, headers, responseEntity.getStatusCode());
            }
        };
    }
}