package fr.agaspardcilia.homeadmin.configuration.properties;

import lombok.Data;

/**
 * Mail configuration.
 */
@Data
public class Mail {
    private String url;
    private String mailAddress;
}
