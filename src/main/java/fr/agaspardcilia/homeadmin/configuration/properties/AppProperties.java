package fr.agaspardcilia.homeadmin.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
@Data
public class AppProperties {
    private String frontUrl;
    private Mail mail;
    private Security security;
}
