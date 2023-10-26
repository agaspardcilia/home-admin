package fr.agaspardcilia.homeadmin.user;

import fr.agaspardcilia.homeadmin.configuration.properties.AppProperties;
import fr.agaspardcilia.homeadmin.configuration.properties.Mail;
import fr.agaspardcilia.homeadmin.mail.MailDto;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class MailFactoryTest {
    @Test
    void testActivationEmail() {
        Mail mail = new Mail();
        mail.setMailAddress("bar");
        AppProperties props = new AppProperties();
        props.setMail(mail);
        MailFactory mailFactory = new MailFactory(props);
        UUID activationId = UUID.randomUUID();
        MailDto actual = mailFactory.getRegistrationActivationCodeMail("foo", activationId);
        MailDto expected = new MailDto(
                "bar",
                "foo",
                "Confirm account creation",
                """
                            Hello,
                                                
                            An account has been created using this mail address, go to null/users/activate/%s to activate it!
                                                
                            Have a nice day.
                        """.formatted(activationId)
        );

        assertEquals(expected, actual);
    }
}
