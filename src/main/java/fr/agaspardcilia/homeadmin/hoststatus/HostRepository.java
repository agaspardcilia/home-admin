package fr.agaspardcilia.homeadmin.hoststatus;

import fr.agaspardcilia.homeadmin.action.Action;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface HostRepository extends JpaRepository<Host, UUID> {
}
