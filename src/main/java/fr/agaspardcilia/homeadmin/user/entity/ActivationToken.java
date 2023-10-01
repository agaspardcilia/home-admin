package fr.agaspardcilia.homeadmin.user.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

/**
 * An activation token. Will be sent when creating a new account.
 */
@Entity
@Data
@NoArgsConstructor
public class ActivationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @OneToOne
    @JoinColumn(name= "user_id")
    private User user;
    @CreationTimestamp
    private Instant creationDate;
}
