package com.timadeshola.vendingmachine.config;

import com.timadeshola.vendingmachine.core.security.CsrfSecurityRequestMatcher;
import com.timadeshola.vendingmachine.core.security.CustomCorsFilter;
import com.timadeshola.vendingmachine.core.security.RestAccessDeniedHandler;
import com.timadeshola.vendingmachine.core.security.RestAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Project title: vending-machine
 *
 * @author johnadeshola
 * Date: 9/16/21
 * Time: 11:10 PM
 */
@Configuration
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    private final RestAccessDeniedHandler restAccessDeniedHandler;
    private final CsrfSecurityRequestMatcher csrfSecurityRequestMatcher;

    @Override
    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        List<RequestMatcher> csrfMethods = new ArrayList<>();
        Arrays.asList("POST", "PUT", "PATCH", "DELETE")
                .forEach(method -> csrfMethods
                        .add(new AntPathRequestMatcher("/**", method)));
        http
                .requestMatcher(new RequestHeaderRequestMatcher("Authorization"))
                .exceptionHandling()
                .authenticationEntryPoint(restAuthenticationEntryPoint)
                .accessDeniedHandler(restAccessDeniedHandler)
                .and().authorizeRequests()
                .antMatchers("/actuator/**", "/swagger-ui**", "/oauth/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().headers().defaultsDisabled().cacheControl()
                .and().xssProtection().block(false)
                .and().frameOptions().sameOrigin().httpStrictTransportSecurity().disable()
                .and().csrf().requireCsrfProtectionMatcher(csrfSecurityRequestMatcher).csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).disable()
                .cors()
                .and().addFilterBefore(new CustomCorsFilter(), ChannelProcessingFilter.class);
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(HttpMethod.POST, "/users");
        web.ignoring().antMatchers(HttpMethod.GET, "/webjars/**", "/*.html", "/favicon.ico", "/**/*.html", "/**/*.css", "/**/*.js");
        web.ignoring()
                .antMatchers("/v1/api-docs")
                .antMatchers("/v2/api-docs")
                .antMatchers("/swagger-resources/**")
                .antMatchers("swagger-ui.html")
                .antMatchers("/configuration/**")
                .antMatchers("/webjars/**")
                .antMatchers("/public/**")
                .antMatchers("/actuator/**");

        web.httpFirewall(allowUrlEncodedSlashHttpFirewall());
    }

    @Bean
    public HttpFirewall allowUrlEncodedSlashHttpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setAllowUrlEncodedSlash(true);
        return firewall;
    }

}