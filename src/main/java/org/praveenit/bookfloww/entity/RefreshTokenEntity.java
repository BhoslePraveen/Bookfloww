package org.praveenit.bookfloww.entity;

import java.time.Instant;

import jakarta.persistence.*;

@Entity
@Table(name = "refresh_tokens")
public class RefreshTokenEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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


	// getters and setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTokenHash() {
		return tokenHash;
	}

	public void setTokenHash(String tokenHash) {
		this.tokenHash = tokenHash;
	}

	public String getGoogleRefreshTokenEnc() {
		return googleRefreshTokenEnc;
	}

	public void setGoogleRefreshTokenEnc(String googleRefreshTokenEnc) {
		this.googleRefreshTokenEnc = googleRefreshTokenEnc;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Instant getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Instant expiryDate) {
		this.expiryDate = expiryDate;
	}

	public TokenStatus getStatus() {
		return status;
	}

	public void setStatus(TokenStatus status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "RefreshTokenEntity [id=" + id + ", tokenHash=" + tokenHash + ", googleRefreshTokenEnc="
				+ googleRefreshTokenEnc + ", expiryDate=" + expiryDate + ", status=" + status + "]";
	}

	

	

	

}
