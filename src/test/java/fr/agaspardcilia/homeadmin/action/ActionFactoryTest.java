package fr.agaspardcilia.homeadmin.action;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ActionFactoryTest {

    @Test
    void testGetAction() {
        Action actual = ActionFactory.getAction("foo");
        assertEquals(
                new Action(actual.getId(), "foo", "foo", true, null, null),
                actual
        );
    }
}
