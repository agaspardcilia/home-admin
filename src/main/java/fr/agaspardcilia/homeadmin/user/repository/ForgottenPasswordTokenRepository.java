package fr.agaspardcilia.homeadmin.user.repository;

import fr.agaspardcilia.homeadmin.user.entity.ForgottenPasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ForgottenPasswordTokenRepository extends JpaRepository<ForgottenPasswordToken, UUID> {
}
