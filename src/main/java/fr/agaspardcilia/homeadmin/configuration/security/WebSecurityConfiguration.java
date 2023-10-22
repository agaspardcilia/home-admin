package fr.agaspardcilia.homeadmin.configuration.security;

import fr.agaspardcilia.homeadmin.authentication.TokenProvider;
import fr.agaspardcilia.homeadmin.security.Authority;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

/**
 * Configures the application's endpoints security policy.
 */
@Configuration
@AllArgsConstructor
public class WebSecurityConfiguration {
    private final CorsFilter corsFilter;
    private final TokenProvider tokenProvider;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.apply(new JwtConfigurer(tokenProvider));
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
                .headers(e -> e.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .sessionManagement(e -> e.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(e -> e
                        .requestMatchers("/authenticate").permitAll()
                        .requestMatchers("/users/register").permitAll()
                        .requestMatchers("/users/password").permitAll()
                        .requestMatchers("/users/activate/*").permitAll()
                        .requestMatchers("/users/forgotten/*").permitAll()
                        .requestMatchers("/users/reset").permitAll()
                        .requestMatchers("/users/current").permitAll()
                        .requestMatchers("/articles/available").permitAll()
                        .requestMatchers("/articles/category/*").permitAll()
                        .requestMatchers("/users/**").hasAnyAuthority(Authority.ADMIN.name())
                        .anyRequest().authenticated()
                ).build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers(HttpMethod.OPTIONS, "/**");
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider result = new DaoAuthenticationProvider();
        result.setUserDetailsService(userDetailsService);
        result.setPasswordEncoder(passwordEncoder);
        return result;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
