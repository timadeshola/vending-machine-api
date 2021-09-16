package com.timadeshola.vendingmachine.service;

import lombok.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * Project title: vending-machine
 *
 * @author johnadeshola
 * Date: 9/16/21
 * Time: 11:28 PM
 */
public interface AuthenticationFacadeService  {

    public Authentication getAuthentication();

    public void setAuthentication(Authentication authentication);

    public List<String> getAuthorities();

    public String getPrincipal();

    Collection<? extends GrantedAuthority> getGrantedAuthorities();
}