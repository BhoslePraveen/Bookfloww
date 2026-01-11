package org.praveenit.bookfloww.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

import java.time.Instant;

@Setter
@Getter
@Entity
@Table(name = "refresh_tokens",
        indexes = {
                @Index(name = "idx_refresh_token_token_id", columnList = "token_id"),
                @Index(name = "idx_refresh_token_status", columnList = "status"),
                @Index(name = "idx_refresh_token_user", columnList = "user_id")
        })
@SQLRestriction("status = 'ACTIVE'")
public class RefreshTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Public lookup identifier (UUID)
     * Used for fast DB search
     */
    @Column(name = "token_id", nullable = false, unique = true, length = 36)
    private String tokenId;

    /**
     * Secure hash of the raw refresh token
     */
    @Column(name = "token_hash", nullable = false, length = 256)
    private String tokenHash;

    /**
     * Encrypted Google refresh token (if OAuth login)
     */
    @Column(name = "google_refresh_token_enc", length = 2048)
    private String googleRefreshTokenEnc;

    @Column(name = "expiry_date", nullable = false)
    private Instant expiryDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TokenStatus status = TokenStatus.ACTIVE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public enum TokenStatus {
        ACTIVE,
        INACTIVE
    }
}
