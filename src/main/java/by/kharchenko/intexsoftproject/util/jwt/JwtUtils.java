package by.kharchenko.intexsoftproject.util.jwt;

import by.kharchenko.intexsoftproject.model.entity.Authority;
import by.kharchenko.intexsoftproject.model.entity.AuthorityType;
import by.kharchenko.intexsoftproject.model.entity.Role;
import by.kharchenko.intexsoftproject.model.entity.RoleType;
import by.kharchenko.intexsoftproject.security.JwtAuthentication;
import io.jsonwebtoken.Claims;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JwtUtils {

    public static JwtAuthentication generate(Claims claims) {
        final JwtAuthentication jwtInfoToken = new JwtAuthentication();
        jwtInfoToken.setUserId(Long.parseLong(claims.getSubject()));
        Set<Role> roles = getRoles(claims);
        Set<Authority> authoritiesFromRoles = getAuthorities(claims);
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        roles.forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getRole().toString())));
        authoritiesFromRoles.forEach(authority -> authorities.add(new SimpleGrantedAuthority(authority.getAuthority().toString())));
        jwtInfoToken.setAuthorities(authorities);
        jwtInfoToken.setAuthenticated(true);
        return jwtInfoToken;
    }

    private static Set<Authority> getAuthorities(Claims claims) {
        Set<Authority> resultAuthorities = new HashSet<>();
        final List<LinkedHashMap<Object, Object>> roles = claims.get("roles", List.class);
        for (LinkedHashMap<Object, Object> roleInfo : roles) {
            for (Map.Entry<Object, Object> entry : roleInfo.entrySet()) {
                if (entry.getKey().equals("authority")) {
                    ArrayList<LinkedHashMap<String, Object>> authorities = (ArrayList<LinkedHashMap<String, Object>>) entry.getValue();
                    for (LinkedHashMap<String, Object> authority : authorities) {
                        Long id = Integer.toUnsignedLong((Integer) authority.get("id"));
                        AuthorityType authorityType = AuthorityType.valueOf((String) authority.get("authority"));
                        resultAuthorities.add(new Authority(id, authorityType));
                    }
                }
            }
        }
        return resultAuthorities;
    }

    private static Set<Role> getRoles(Claims claims) {
        Set<Role> resultRoles = new HashSet<>();
        final List<LinkedHashMap<Object, Object>> roles = claims.get("roles", List.class);
        for (LinkedHashMap<Object, Object> roleInfo : roles) {
            Role userRole = new Role();
            for (Map.Entry<Object, Object> entry : roleInfo.entrySet()) {
                if (entry.getKey().equals("id")) {
                    userRole.setId(Long.parseLong(entry.getValue().toString()));
                }
                if (entry.getKey().equals("role")) {
                    userRole.setRole(RoleType.valueOf(entry.getValue().toString()));
                }
            }
            resultRoles.add(userRole);
        }

        return resultRoles;
    }
}

