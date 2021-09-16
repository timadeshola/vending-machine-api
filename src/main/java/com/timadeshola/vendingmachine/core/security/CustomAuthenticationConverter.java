package com.timadeshola.vendingmachine.core.security;

import com.timadeshola.vendingmachine.core.utils.AppUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Project title: vending-machine
 *
 * @author johnadeshola
 * Date: 9/16/21
 * Time: 10:30 PM
 */
@Slf4j
public class CustomAuthenticationConverter implements UserAuthenticationConverter {

    private final String username = "username";

    private Collection<? extends GrantedAuthority> defaultAuthorities;

    public void setDefaultAuthorities(String[] defaultAuthorities) {
        this.defaultAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList(StringUtils.arrayToCommaDelimitedString(defaultAuthorities));
    }

    @Override
    public Map<String, ?> convertUserAuthentication(Authentication userAuthentication) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put(username, userAuthentication.getName());

        if (userAuthentication.getAuthorities() != null && !userAuthentication.getAuthorities().isEmpty())
            response.put(AUTHORITIES, AuthorityUtils.authorityListToSet(userAuthentication.getAuthorities()));

        return response;
    }

    @Override
    public Authentication extractAuthentication(Map<String, ?> map) {
        log.info("#################### map value ##############################::: {}", AppUtil.toJson(map));
        if (map.containsKey(username)) {
            return new UsernamePasswordAuthenticationToken(AppUtil.getPrincipal(), null, getAuthorities(map));
        }
        return null;
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Map<String, ?> map) {
        if (!map.containsKey(AUTHORITIES))
            return defaultAuthorities;

        Object authorities = map.get(AUTHORITIES);

        if (authorities instanceof String)
            return AuthorityUtils.commaSeparatedStringToAuthorityList((String) authorities);

        if (authorities instanceof Collection)
            return AuthorityUtils.commaSeparatedStringToAuthorityList(
                    StringUtils.collectionToCommaDelimitedString((Collection<?>) authorities));

        throw new IllegalArgumentException("Authorities must be either a String or a Collection");
    }
}