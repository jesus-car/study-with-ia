package com.studytech.studytech.presentation.router;

import com.studytech.studytech.presentation.handler.UserHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class UserRouter {
    @Bean
    public RouterFunction<ServerResponse> userRoutes(UserHandler userHandler) {
        return route()
                .GET("/users", userHandler::getAllUsers)
                .GET("/users/{id}", userHandler::getUserById)
                .PUT("/users/{id}", userHandler::updateUser)
                .DELETE("/users/{id}", userHandler::deleteUser)
                .build();
    }
}
