package fr.agaspardcilia.homeadmin.configuration.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.agaspardcilia.homeadmin.authentication.TokenProvider;
import lombok.AllArgsConstructor;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * A security adapter adding a JWT filter for authentication.
 */
@AllArgsConstructor
public class JwtConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private final TokenProvider tokenProvider;
    private final ObjectMapper objectMapper;

    @Override
    public void configure(HttpSecurity httpSecurity) {
        httpSecurity.addFilterBefore(new JwtFilter(objectMapper, tokenProvider), UsernamePasswordAuthenticationFilter.class);
    }
}
