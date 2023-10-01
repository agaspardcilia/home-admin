package fr.agaspardcilia.homeadmin.authentication;

import fr.agaspardcilia.homeadmin.authentication.exception.UserNotActivatedException;
import fr.agaspardcilia.homeadmin.common.exception.ApiForbiddenException;
import fr.agaspardcilia.homeadmin.common.exception.ApiNotFoundException;
import fr.agaspardcilia.homeadmin.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Custom user details service. Makes the link between spring security users and database users.
 */
@Component("userDetailsService")
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomUserDetailsService.class);

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
        LOGGER.debug("Authenticating {}", mail);
        // Api exceptions should not be thrown from a service. This is a special case.
        fr.agaspardcilia.homeadmin.user.entity.User user = userRepository.findByMail(mail.toLowerCase())
                .orElseThrow(() -> new ApiNotFoundException("Unable to find user with mail: %s".formatted(mail)));
        try {
            return createSpringSecurityUser(user);
        } catch (UserNotActivatedException e) {
            throw new ApiForbiddenException(e.getMessage());
        }
    }

    private User createSpringSecurityUser(fr.agaspardcilia.homeadmin.user.entity.User user) throws UserNotActivatedException {
        if (user.getIsActive() == null || !user.getIsActive()) {
            throw new UserNotActivatedException();
        }
        Set<SimpleGrantedAuthority> authorities = user.getAuthorities().stream()
                .map(Enum::name)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toUnmodifiableSet());
        return new User(user.getMail(), user.getPassword(), authorities);
    }
}
