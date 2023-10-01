package fr.agaspardcilia.homeadmin.user.repository;

import fr.agaspardcilia.homeadmin.user.entity.ActivationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ActivationTokenRepository extends JpaRepository<ActivationToken, UUID> {
}
