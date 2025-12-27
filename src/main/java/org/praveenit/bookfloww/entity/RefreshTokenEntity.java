package org.praveenit.bookfloww.entity;

import java.time.Instant;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "refresh_tokens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"user", "googleRefreshTokenEnc", "tokenHash"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class RefreshTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    // HASHED UUID TOKEN
    @Column(nullable = false, unique = true, length = 256)
    private String tokenHash;

    // ENCRYPTED GOOGLE REFRESH TOKEN
    @Column(length = 2048)
    private String googleRefreshTokenEnc;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Instant expiryDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TokenStatus status = TokenStatus.ACTIVE;

    public enum TokenStatus {
        ACTIVE,
        INACTIVE
    }
}
