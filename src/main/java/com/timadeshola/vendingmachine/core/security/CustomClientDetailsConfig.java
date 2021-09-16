package com.timadeshola.vendingmachine.core.security;

import com.timadeshola.vendingmachine.persistence.entity.CustomClientDetails;
import com.timadeshola.vendingmachine.persistence.repository.CustomClientDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.stream.Collectors;

/**
 * Project title: vending-machine
 *
 * @author johnadeshola
 * Date: 9/16/21
 * Time: 10:30 PM
 */
@Service
@RequiredArgsConstructor
public class CustomClientDetailsConfig implements ClientDetailsService {

    private final CustomClientDetailRepository customClientDetailRepository;

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {

        if (!clientId.isEmpty()) {
            CustomClientDetails client = customClientDetailRepository.findByClientId(clientId).<BadCredentialsException>orElseThrow(() -> {
                throw new BadCredentialsException("client information cannot be found");
            });

            String resourceIds = String.join(",", client.getResourceIds());
            String scopes = String.join(",", client.getScope());
            String grantTypes = String.join(",", client.getAuthorizedGrantTypes());
            String authorities = client.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));

            BaseClientDetails clientDetails = new BaseClientDetails(client.getClientId(), resourceIds, scopes, grantTypes, authorities);

            clientDetails.setClientSecret(client.getClientSecret());
            clientDetails.setAutoApproveScopes(Collections.singletonList(scopes));
            clientDetails.setAccessTokenValiditySeconds(client.getAccessTokenValiditySeconds());
            clientDetails.setRefreshTokenValiditySeconds(client.getRefreshTokenValiditySeconds());
            clientDetails.setRegisteredRedirectUri(Collections.singleton(client.getWebServerRedirectUri()));
            return clientDetails;

        } else {
            throw new BadCredentialsException("Client ID cannot be empty");
        }
    }
}