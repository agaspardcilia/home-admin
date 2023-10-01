package fr.agaspardcilia.homeadmin.user.repository;

import fr.agaspardcilia.homeadmin.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByMail(String mail);
    boolean existsByMail(String mail);
}
