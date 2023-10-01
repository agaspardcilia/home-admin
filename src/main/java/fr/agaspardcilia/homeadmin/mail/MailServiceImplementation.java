package fr.agaspardcilia.homeadmin.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * Provides mail services. This is the production mail service.
 */
@Service
@Profile("mail")
public class MailServiceImplementation implements MailService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MailServiceImplementation.class);

    /**
     * Constructor.
     */
    public MailServiceImplementation() {
        LOGGER.info("Using production mail service, mails will be sent");
    }

    @Override
    public void sendMail(MailDto mail) {
        LOGGER.warn("Cannot send mail, not implemented yet!");
    }
}
