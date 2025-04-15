package io.tanibilet.server.auth;

import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

public record UserPrincipal(
        String userId,
        String email,
        String username,
        Boolean emailVerified,
        String firstname,
        String lastname,
        Set<GrantedAuthority> authorities
) {
}
