package com.timadeshola.vendingmachine.core.security;

import com.timadeshola.vendingmachine.persistence.repository.UserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;

import java.util.Map;

/**
 * Project title: vending-machine
 *
 * @author johnadeshola
 * Date: 9/16/21
 * Time: 10:30 PM
 */
public class CustomOAuth2RequestFactory extends DefaultOAuth2RequestFactory {

    private final TokenStore tokenStore;
    private final UserRepository userRepository;

    public CustomOAuth2RequestFactory(ClientDetailsService clientDetailsService, TokenStore tokenStore, UserRepository userRepository) {
        super(clientDetailsService);
        this.tokenStore = tokenStore;
        this.userRepository = userRepository;
    }

    @Override
    public TokenRequest createTokenRequest(Map<String, String> requestParameters, ClientDetails authenticatedClient) {
        if (requestParameters.get("grant_type").equals("refresh_token")) {
            OAuth2Authentication authentication = tokenStore.readAuthenticationForRefreshToken(tokenStore.readRefreshToken(requestParameters.get("refresh_token")));
            userRepository.findByUsername(authentication.getName()).ifPresent(user ->
                    SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())));
            return super.createTokenRequest(requestParameters, authenticatedClient);
        }
        String username = requestParameters.get("username");

        userRepository.findByUsername(username).ifPresent(user ->
                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())));
        return super.createTokenRequest(requestParameters, authenticatedClient);
    }
}