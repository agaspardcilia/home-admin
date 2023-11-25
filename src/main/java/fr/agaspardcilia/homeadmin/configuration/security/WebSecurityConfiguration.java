package fr.agaspardcilia.homeadmin.configuration.security;

import fr.agaspardcilia.homeadmin.authentication.TokenProvider;
import fr.agaspardcilia.homeadmin.configuration.FilterConfigurer;
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
        httpSecurity.apply(new FilterConfigurer(tokenProvider));
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
                .headers(e -> e.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .sessionManagement(e -> e.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(e -> e
                        .requestMatchers(
                                "/",
                                "/index.html",
                                "/static/**",
                                "/*.ico",
                                "/*.json",
                                "/*.png",
                                "/resources/**"
                        ).permitAll()
                        .requestMatchers("/api/authenticate").permitAll()
                        .requestMatchers("/api/users/register").permitAll()
                        .requestMatchers("/api/users/password").permitAll()
                        .requestMatchers("/api/users/activate/*").permitAll()
                        .requestMatchers("/api/users/forgotten/*").permitAll()
                        .requestMatchers("/api/users/reset").permitAll()
                        .requestMatchers("/api/users/current").permitAll()
                        .requestMatchers("/api/articles/available").permitAll()
                        .requestMatchers("/api/articles/category/*").permitAll()
                        .requestMatchers("/api/users/**").hasAnyAuthority(Authority.ADMIN.name())
                        .anyRequest().authenticated()
                ).build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web
                .ignoring()
                .requestMatchers(HttpMethod.OPTIONS, "/**");
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
