package fr.agaspardcilia.homeadmin.authentication;

import fr.agaspardcilia.homeadmin.authentication.exception.InvalidTokenException;
import fr.agaspardcilia.homeadmin.configuration.properties.AppProperties;
import fr.agaspardcilia.homeadmin.configuration.properties.Security;
import fr.agaspardcilia.homeadmin.security.Authority;
import fr.agaspardcilia.homeadmin.user.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TokenProviderTest {
    @Test
    void testCreateToken() throws InvalidTokenException {
        TokenProvider provider = new TokenProvider(props());
        UserDto user = user();
        Authentication authentication = authentication(user.id().toString());

        String token = provider.createToken(user, authentication, true);
        Authentication toTest = provider.getAuthentication(token);

        assertEquals(authentication, toTest);
    }

    private UserDto user() {
        return new UserDto(
                UUID.randomUUID(),
                "foo@bar.fr",
                true,
                true
        );
    }

    private Authentication authentication(String credentials) {
        return new UsernamePasswordAuthenticationToken("foo", credentials, Set.of(new SimpleGrantedAuthority(Authority.USER.name())));
    }

    private AppProperties props() {
        Security.Jwt jwt = new Security.Jwt();
        jwt.setSecret("M2Y4N2YxNmY0Y2E3NDQ2ZmQxOWExM2U1MDhmYTUxODI4MmE4NWNmNWVkZTRhNGUwNzczYjBkZDA5ZDAyZjE2NjVlNDUwZTEyNjViYmFkZDQ5YzkwNGVmMzc2NTRiMDgyODdhYmY4Y2Q0YmM4NGRiNzJlMWZiYjM4ZDZjOTI5Mzg=");
        jwt.setTokenValidityInSeconds(10L);
        jwt.setTokenValidityInSecondsForRememberMe(20L);

        Security security = new Security();
        security.setJwt(jwt);

        AppProperties result = new AppProperties();
        result.setSecurity(security);
        return result;
    }
}
