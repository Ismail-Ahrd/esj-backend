package ma.inpt.esj.utils;

import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    public Jwt getJwtFromAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof Jwt) {
            return (Jwt) authentication.getPrincipal();
        }
        throw new IllegalArgumentException("Authentication principal is not of type Jwt");
    }

    public Long getUserIdFromJwt() {
        Jwt jwt = getJwtFromAuthentication();
        Map<String, Object> claims = (Map) jwt.getClaims().get("claims");
        return (Long) claims.get("id");
    }
}
