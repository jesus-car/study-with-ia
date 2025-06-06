package com.studytech.studytech.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class SecurityContextRepository implements ServerSecurityContextRepository {

    private final JwtAuthenticationManager jwtAuthenticationManager;

    // Metodo se usa para guardar el SecurityContext en la sesion o chache
    // Con JWT no es necesario
    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        return Mono.empty();
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        // Extrae el token que se agrego en el JwtFilter
        // Aunque tambien se pudo extraer aca, pero da igual
        String token = exchange.getAttribute("token");
        // Esto se manda al JwtAuthenticationManager
        // Si es valido devuelve un Mono<Authenticate> y se lo transforma en un Mono<SecurityContext>
        // Es necesario construir el objeto UsernamePasswordAuthenticationToken
        // porque eso lo pide la firma del AuthenticationManager
        return jwtAuthenticationManager.authenticate(new UsernamePasswordAuthenticationToken(token,token))
                .map(SecurityContextImpl::new);
    }
}