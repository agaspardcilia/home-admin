package fr.agaspardcilia.homeadmin.user.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

/**
 * Represents a token that should be used to reset a password.
 */
@Entity
@Data
@NoArgsConstructor
public class ForgottenPasswordToken {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @OneToOne
    @JoinColumn(name= "user_id")
    private User user;
    @CreationTimestamp
    private Instant creationDate;
}
