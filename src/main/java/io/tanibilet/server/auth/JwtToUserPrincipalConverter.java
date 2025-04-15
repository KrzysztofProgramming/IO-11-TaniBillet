package io.tanibilet.server.auth;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;
import java.util.stream.Collectors;

public class JwtToUserPrincipalConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private static final String REALM_ACCESS = "realm_access";
    private static final String RESOURCE_ACCESS = "resource_access";
    // Nazwa klienta, którego role Cię interesują (z resource_access)
    private static final String CLIENT_ID = "tani-bilet-app";

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        // 1. Wydobywamy dane użytkownika z tokenu
        String userId = jwt.getClaimAsString("sub");
        String email = jwt.getClaimAsString("email");
        Boolean emailVerified = jwt.getClaimAsBoolean("email_verified");
        String firstName = jwt.getClaimAsString("given_name");
        String lastName = jwt.getClaimAsString("family_name");
        String username = jwt.getClaimAsString("preferred_username");

        // 2. Zbieramy role (zarówno z realm_access, jak i resource_access)
        Set<String> roles = extractRoles(jwt);

        // 3. Tworzymy GrantedAuthorities
        Set<GrantedAuthority> authorities = roles.stream()
                .map(roleName -> new SimpleGrantedAuthority("ROLE_" + roleName))
                .collect(Collectors.toSet());

        // 4. Tworzymy naszego UserPrincipal
        UserPrincipal userPrincipal = new UserPrincipal(
                userId,
                email,
                username,
                emailVerified,
                firstName,
                lastName,
                authorities
        );

        // 5. Pakujemy do obiektu Authentication
        //    Drugi argument (credentials) zwykle dajemy null lub jwt.getTokenValue() jeśli chcesz przechować surowy token
        return new UsernamePasswordAuthenticationToken(
                userPrincipal,
                jwt.getTokenValue(),  // lub null
                authorities
        );
    }

    /**
     * Przykładowa metoda wyciągająca role z `realm_access.roles` oraz
     * `resource_access[CLIENT_ID].roles`
     */
    private Set<String> extractRoles(Jwt jwt) {
        Set<String> roles = new HashSet<>();

        // 1. Roles z "realm_access"
        Map<String, Object> realmAccess = jwt.getClaim(REALM_ACCESS);
        if (realmAccess instanceof Map<?, ?> raMap) {
            Object realmRoles = raMap.get("roles");
            if (realmRoles instanceof Collection<?> rr) {
                rr.forEach(role -> roles.add(role.toString()));
            }
        }

        // 2. Roles z "resource_access" -> "tani-bilet-app" -> "roles"
        Map<String, Object> resourceAccess = jwt.getClaim(RESOURCE_ACCESS);
        if (resourceAccess instanceof Map<?, ?> resMap) {
            Object clientAccess = resMap.get(CLIENT_ID);
            if (clientAccess instanceof Map<?, ?> clientMap) {
                Object clientRoles = clientMap.get("roles");
                if (clientRoles instanceof Collection<?> cr) {
                    cr.forEach(role -> roles.add(role.toString()));
                }
            }
        }
        return roles;
    }
}
