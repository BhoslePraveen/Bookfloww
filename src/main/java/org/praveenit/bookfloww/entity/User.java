package org.praveenit.bookfloww.entity;

import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.*;
import lombok.*;
import org.praveenit.bookfloww.entity.audit.AuditableEntity;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "refreshTokens")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    // nullable initially, unique later
    @Column(unique = true)
    private String customerCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role = UserRole.CLIENT;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @OneToMany(
        mappedBy = "user",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private Set<RefreshTokenEntity> refreshTokens = new HashSet<>();

    public enum UserRole {
        ADMIN, PROVIDER, CLIENT
    }
}
