package fr.agaspardcilia.homeadmin.user;

import fr.agaspardcilia.homeadmin.configuration.properties.AppProperties;
import fr.agaspardcilia.homeadmin.mail.MailDto;
import lombok.AllArgsConstructor;

import java.util.UUID;

/**
 * A factory producing various mails.
 */
@AllArgsConstructor
class MailFactory {
    private final AppProperties properties;

    /**
     * TODO: test me!
     * Creates a registration activation code mail.
     *
     * @param to the destination.
     * @param activationToken the activation token.
     * @return the mail.
     */
    public MailDto getRegistrationActivationCodeMail(String to, UUID activationToken) {
        return new MailDto(
                properties.getMail().getMailAddress(),
                to,
                "Confirm account creation",
                """
                    Hello,
    
                    An account has been created using this mail address, go to %s/users/activate/%s to activate it!
    
                    Have a nice day.
                """.formatted(properties.getFrontUrl(), activationToken.toString()).stripIndent()
                );
    }
}
