package fr.agaspardcilia.homeadmin.mail;

/**
 * A mail.
 *
 * @param from
 * @param to
 * @param subject
 * @param content
 */
public record MailDto(
    String from,
    String to,
    String subject,
    String content
) { }
