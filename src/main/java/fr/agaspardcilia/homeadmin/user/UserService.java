package fr.agaspardcilia.homeadmin.user;

import fr.agaspardcilia.homeadmin.common.util.AuthenticationUtil;
import fr.agaspardcilia.homeadmin.mail.MailService;
import fr.agaspardcilia.homeadmin.user.entity.ActivationToken;
import fr.agaspardcilia.homeadmin.user.entity.User;
import fr.agaspardcilia.homeadmin.user.repository.ActivationTokenRepository;
import fr.agaspardcilia.homeadmin.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 * Does user stuff.
 */
@Service
@AllArgsConstructor
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final ActivationTokenRepository activationTokenRepository;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;
    private final MailFactory mailFactory;

    /**
     * Retrieves a user from its ID.
     *
     * @param id the ID of the user.
     * @return the user, if found.
     */
    public Optional<UserDto> get(UUID id) {
        return userRepository.findById(id)
                .map(UserDto::from);
    }

    /**
     * Retrieves a user from its mail.
     *
     * @param mail the mail of the user.
     * @return the user, if found.
     */
    public Optional<UserDto> get(String mail) {
        return userRepository.findByMail(mail)
                .map(UserDto::from);
    }


    /**
     * Checks if a mail is taken by a user.
     *
     * @param mail the mail.
     * @return true if it is.
     */
    public boolean existByMail(String mail) {
        return userRepository.existsByMail(mail);
    }

    /**
     * Registers a user. Disabled for now as the application does not require registering users.
     *
     * @param request the registration request.
     * @return the created user.
     * @throws UserAlreadyExistsException if the user already exists.
     */
    @Transactional
    public UserDto register(UserCreationRequest request) throws UserAlreadyExistsException {
        User toSave = new User();
        if (existByMail(request.getMail())) {
            throw new UserAlreadyExistsException();
        }

        toSave.setMail(request.getMail());
        toSave.setPassword(passwordEncoder.encode(request.getPassword()));

        User savedUser = userRepository.save(toSave);
        UUID activationToken = createActivationToken(savedUser);

        LOGGER.debug("Registering {} with {} as his activation key", savedUser.getMail(), activationToken);
        sendUserRegistrationMail(savedUser, activationToken);

        return UserDto.from(savedUser);
    }

    /**
     * @return the currently authenticated user.
     */
    public Optional<UserDto> getCurrentUser() {
        return AuthenticationUtil.getCurrentUserId()
                .map(this::get)
                .filter(Optional::isPresent)
                .map(Optional::get);
    }

    private UUID createActivationToken(User user) {
        ActivationToken activationToken = new ActivationToken();
        activationToken.setUser(user);
        return activationTokenRepository.save(activationToken).getId();
    }

    private void sendUserRegistrationMail(User user, UUID activationToken) {
        mailService.sendMail(mailFactory.getRegistrationActivationCodeMail(user.getMail(), activationToken));
    }
}
