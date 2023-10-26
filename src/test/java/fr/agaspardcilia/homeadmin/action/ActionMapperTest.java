package fr.agaspardcilia.homeadmin.action;

import fr.agaspardcilia.homeadmin.action.dto.ActionDto;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ActionMapperTest {

    @Test
    void toDto() {
        Action action = new Action(UUID.randomUUID(), "foo", "bar", true, Instant.MIN, Instant.MAX);
        assertEquals(
                new ActionDto(action.getId(), action.getName(), action.getRunnableExists(), action.getCreationDate(), action.getUpdateDate()),
                ActionMapper.toDto(action)
        );
    }
}
