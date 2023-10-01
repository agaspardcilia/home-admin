package fr.agaspardcilia.homeadmin.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.agaspardcilia.homeadmin.security.Authority;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * A user.
 */
@Entity(name = "app_user")
@Data
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String mail;
    private String password;
    @ElementCollection(targetClass = Authority.class, fetch = FetchType.EAGER)
    @JsonIgnore
    @CollectionTable(name = "user_authority", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Authority> authorities = new HashSet<>();
    private Boolean isActive;
    @CreationTimestamp
    private Instant creationDate;
    @UpdateTimestamp
    private Instant updateDate;
}
