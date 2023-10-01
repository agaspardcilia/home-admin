package fr.agaspardcilia.homeadmin.mail;

import org.springframework.scheduling.annotation.Async;

/**
 * A mail service provider contract.
 */
public interface MailService {

    /**
     * Sends a mail. This operation should not be synchronous.
     *
     * @param mail the mail to send.
     */
    @Async
    void sendMail(MailDto mail);
}
