package fr.agaspardcilia.homeadmin.action;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ActionRepository extends JpaRepository<Action, UUID> {
    boolean existsByIdNotAndName(UUID id, String name);
}
