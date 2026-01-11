package org.praveenit.bookfloww.dto.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoogleUserProfile {
    private String email;
    private String name;
    private String sub;
    private Boolean email_verified;
    private String picture;
}
