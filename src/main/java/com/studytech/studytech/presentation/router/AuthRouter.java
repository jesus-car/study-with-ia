package com.studytech.studytech.presentation.router;

import com.studytech.studytech.presentation.handler.AuthHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class AuthRouter {

    @Bean
    public RouterFunction<ServerResponse> authRoutes(AuthHandler authHandler) {
        return route()
                .POST("/auth/login", authHandler::login)
                .POST("/auth/register", authHandler::register)
                .build();
    }
}
