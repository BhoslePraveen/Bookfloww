package org.praveenit.bookfloww.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "users")
@SQLDelete(sql = "UPDATE users SET is_active = false WHERE id = ?")
@SQLRestriction("is_active = true")
public class User extends AuditStamp {

	@Id
	@GeneratedValue(generator = "customer-id-generator")
	@GenericGenerator(
			name = "customer-id-generator",
			strategy = "org.praveenit.bookfloww.entity.CustomerIdGenerator"
	)
	@Column(length = 10, nullable = false, updatable = false)
	private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

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
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)

    private Set<GoogleEventEntity> googleEvents = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)

    private Set<BookingEntity> bookings = new HashSet<>();
    
    public enum UserRole {
        ADMIN, PROVIDER, CLIENT
    }
}

