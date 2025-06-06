package com.studytech.studytech.security;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationManager implements ReactiveAuthenticationManager {

    private final JwtUtil jwtUtil;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.justOrEmpty(authentication.getCredentials())
                .cast(String.class)
                .flatMap(jwt -> {
                    try {
                        Claims claims = jwtUtil.getAllClaimsAndValidate(jwt);
                        List<SimpleGrantedAuthority> authorities = extractAuthorities(claims);
                        return Mono.just(new UsernamePasswordAuthenticationToken(claims.getSubject(), null, authorities));
                    } catch (Exception e) {
                        return Mono.error(new AuthenticationException("Invalid token", e) {});
                    }
                });
    }

    private List<SimpleGrantedAuthority> extractAuthorities(Claims claims) {
        Object roles = claims.get("roles");
        if (roles instanceof List<?> rolesList) {
            return rolesList.stream()
                    .filter(Map.class::isInstance)
                    .map(role -> (Map<?, ?>) role)
                    .map(roleMap -> roleMap.get("authority"))
                    .filter(String.class::isInstance)
                    .map(String.class::cast)
                    .map(SimpleGrantedAuthority::new)
                    .toList();
        }
        return List.of();
    }
}