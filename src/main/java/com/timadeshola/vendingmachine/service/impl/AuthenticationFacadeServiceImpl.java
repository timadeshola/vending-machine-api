package com.timadeshola.vendingmachine.service.impl;

import com.timadeshola.vendingmachine.service.AuthenticationFacadeService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Project title: vending-machine
 *
 * @author johnadeshola
 * Date: 9/16/21
 * Time: 11:29 PM
 */
@Service
public class AuthenticationFacadeServiceImpl implements AuthenticationFacadeService {

    @Override
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Override
    public void setAuthentication(Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Override
    public List<String> getAuthorities() {
        return getAuthentication().getAuthorities().parallelStream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
    }

    @Override
    public String getPrincipal() {
        return getAuthentication().getName();
    }

    @Override
    public Collection<? extends GrantedAuthority> getGrantedAuthorities() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities();
    }
}