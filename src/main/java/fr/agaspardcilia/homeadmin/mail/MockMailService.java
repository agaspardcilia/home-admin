package fr.agaspardcilia.homeadmin.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * Acts asa mail service but writes them in logs. This is meant to be used as a dev mail service.
 */
@Service
@Profile("!mail")
public class MockMailService implements MailService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MockMailService.class);

    /**
     * Constructor.
     */
    public MockMailService() {
        LOGGER.info("Using mock mail service, no actual mail will be sent");
    }

    @Override
    public void sendMail(MailDto mail) {
        LOGGER.debug(
                "Sending email to {}, from {}:\n{}\n{}",
                mail.to(),
                mail.from(),
                mail.subject(),
                mail.content()
        );
    }
}
