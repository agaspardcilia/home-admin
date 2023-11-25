package fr.agaspardcilia.homeadmin.common.util;

import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AuthenticationUtilTest {

    @Test
    void testPresent() {
        UUID uuid = UUID.randomUUID();
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(uuid.toString(), uuid));
        assertEquals(uuid, AuthenticationUtil.getCurrentUserId().orElseThrow());
    }

    @Test
    void testAbsent() {
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken("", "");
        SecurityContextHolder.getContext().setAuthentication(auth);
        assertTrue(AuthenticationUtil.getCurrentUserId().isEmpty());
    }
}
