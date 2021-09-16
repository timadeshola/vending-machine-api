package com.timadeshola.vendingmachine.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.IOException;
import java.util.*;

/**
 * Project title: vending-machine
 *
 * @author johnadeshola
 * Date: 9/15/21
 * Time: 12:19 PM
 */
@Entity
@Table(name = "client_details")
@Setter
@Getter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CustomClientDetails extends BaseEntity implements ClientDetails {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Column(name = "client_id", length = 100)
    private String clientId;

    @Column(name = "client_secret", length = 120)
    @JsonIgnore
    private String clientSecret;

    @Column(name = "resource_ids", length = 500)
    private String resourceIds;

    @Column(name = "scope", length = 200)
    private String scope;

    @Column(name = "grant_type", length = 200)
    private String authorizedGrantTypes;

    @Column(name = "redirect_url", length = 400)
    private String webServerRedirectUri;

    @Column(name = "authorities", length = 200)
    private String authorities;

    @Column(name = "access_token_validity", length = 10)
    private Integer accessTokenValiditySeconds;

    @Column(name = "refrest_token_validity", length = 10)
    private Integer refreshTokenValiditySeconds;

    @Column(name = "addition_info", length = 200)
    private String additionalInformation;

    @Column(name = "auto_approve", length = 200)
    private String autoApprove;

    @Override
    public Set<String> getResourceIds() {
        if (StringUtils.isEmpty(this.resourceIds)) {
            return new HashSet<>();
        } else {
            return StringUtils.commaDelimitedListToSet(this.resourceIds);
        }
    }

    public void setResourceIds(Set<String> resourceIds) {
        this.resourceIds = StringUtils.collectionToCommaDelimitedString(resourceIds);
    }

    @Override
    public boolean isSecretRequired() {
        return !StringUtils.isEmpty(this.clientSecret);
    }

    @Override
    public String getClientSecret() {
        return this.clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    @Override
    public boolean isScoped() {
        return this.getScope().size() > 0;
    }

    @Override
    public Set<String> getScope() {
        return StringUtils.commaDelimitedListToSet(this.scope);
    }

    public void setScope(Set<String> scope) {
        this.scope = StringUtils.collectionToCommaDelimitedString(scope);
    }

    @Override
    public Set<String> getAuthorizedGrantTypes() {
        return StringUtils.commaDelimitedListToSet(this.authorizedGrantTypes);
    }

    public void setAuthorizedGrantTypes(Set<String> authorizedGrantType) {
        this.authorizedGrantTypes = StringUtils.collectionToCommaDelimitedString(authorizedGrantType);
    }

    @Override
    public Set<String> getRegisteredRedirectUri() {
        return StringUtils.commaDelimitedListToSet(this.webServerRedirectUri);
    }

    public void setRegisteredRedirectUri(Set<String> registeredRedirectUriList) {
        this.webServerRedirectUri = StringUtils.collectionToCommaDelimitedString(registeredRedirectUriList);
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        Set<String> set = StringUtils.commaDelimitedListToSet(this.authorities);
        Set<GrantedAuthority> result = new HashSet<>();
        set.forEach(authority -> result.add((GrantedAuthority) () -> authority));
        return result;
    }

    public void setAuthorities(Set<GrantedAuthority> authorities) {
        this.authorities = StringUtils.collectionToCommaDelimitedString(authorities);
    }

    @Override
    public Integer getAccessTokenValiditySeconds() {
        return this.accessTokenValiditySeconds;
    }

    public void setAccessTokenValiditySeconds(Integer accessTokenValiditySeconds) {
        this.accessTokenValiditySeconds = accessTokenValiditySeconds;
    }

    @Override
    public Integer getRefreshTokenValiditySeconds() {
        return this.refreshTokenValiditySeconds;
    }

    public void setRefreshTokenValiditySeconds(Integer refreshTokenValiditySeconds) {
        this.refreshTokenValiditySeconds = refreshTokenValiditySeconds;
    }

    @Override
    public boolean isAutoApprove(String scope) {
        return this.getAutoApproveScope().contains(scope);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Object> getAdditionalInformation() {
        try {
            return mapper.readValue(this.additionalInformation, Map.class);
        } catch (IOException e) {
            return new HashMap<>();
        }
    }

    public void setAdditionalInformation(Map<String, Object> additionalInformation) {
        try {
            this.additionalInformation = mapper.writeValueAsString(additionalInformation);
        } catch (IOException e) {
            this.additionalInformation = "";
        }
    }

    public Set<String> getAutoApproveScope() {
        return StringUtils.commaDelimitedListToSet(this.autoApprove);
    }

    public void setAutoApproveScope(Set<String> autoApproveScope) {
        this.autoApprove = StringUtils.collectionToCommaDelimitedString(autoApproveScope);
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}