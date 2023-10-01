package fr.agaspardcilia.homeadmin.configuration;

import fr.agaspardcilia.homeadmin.configuration.properties.AppProperties;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.server.WebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * Configures the entire web, I know, powerful.
 */
@Configuration
@AllArgsConstructor
public class WebConfigurer implements ServletContextInitializer, WebServerFactoryCustomizer<WebServerFactory> {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebConfigurer.class);

    private final Environment environment;
    private final AppProperties properties;

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        String profiles = String.join(",", environment.getActiveProfiles());
        if (!StringUtils.isBlank(profiles)) {
            LOGGER.info("Web application configuration, using profiles: {}", profiles);
        }
    }

    @Override
    public void customize(WebServerFactory factory) {
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration configuration = properties.getSecurity().getCors();

        if (configuration.getAllowedOriginPatterns() != null && !configuration.getAllowedOriginPatterns().isEmpty()) {
            LOGGER.info("Registering CORS filters");
            source.registerCorsConfiguration("/**", configuration);
        }

        return new CorsFilter(source);
    }
}
