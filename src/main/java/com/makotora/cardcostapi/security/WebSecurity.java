package com.makotora.cardcostapi.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.makotora.cardcostapi.constants.SecurityConstants;

@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter
{
    @Value(SecurityConstants.PRINCIPAL_REQUEST_HEADER)
    private String principalRequestHeader;

    @Value(SecurityConstants.PRINCIPAL_REQUEST_VALUE)
    private String principalRequestValue;

    public WebSecurity()
    {}

    @Override
    protected void configure(HttpSecurity httpSecurity)
        throws Exception
    {
        APIKeyAuthFilter apiKeyAuthFilter = new APIKeyAuthFilter(principalRequestHeader);
        apiKeyAuthFilter.setAuthenticationManager(new AuthenticationManager() {

            @Override
            public Authentication authenticate(Authentication authentication)
                throws AuthenticationException
            {
                String principal = (String) authentication.getPrincipal();
                if (!principalRequestValue.equals(principal)) {
                    throw new BadCredentialsException("Unexpected API key header or value.");
                }
                authentication.setAuthenticated(true);
                return authentication;
            }
        });

        httpSecurity.cors();

        httpSecurity.csrf().disable();

        httpSecurity.requiresChannel().anyRequest().requiresSecure();

        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        httpSecurity.addFilter(apiKeyAuthFilter)
                    .authorizeRequests()
                    .anyRequest()
                    .authenticated();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource()
    {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }
}
