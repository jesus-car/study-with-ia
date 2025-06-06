package com.studytech.studytech.presentation.handler;

import com.studytech.studytech.persistence.model.UserDocument;
import com.studytech.studytech.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;



@Component
@RequiredArgsConstructor
public class UserHandler {

    private final UserService userService;

    public Mono<ServerResponse> getAllUsers(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(userService.getAllUsers(), UserDocument.class);
    }

    public Mono<ServerResponse> getUserById(ServerRequest serverRequest) {
        String userId = serverRequest.pathVariable("id");
        return userService.getUserById(userId)
                .flatMap(user -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(user))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> updateUser(ServerRequest serverRequest) {
        String userId = serverRequest.pathVariable("id");

        return userService.updateUser(userId, serverRequest)
                .flatMap(user -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(user)
                )
                .onErrorResume(RuntimeException.class, e -> ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(e.getMessage()));
    }

    public Mono<ServerResponse> deleteUser(ServerRequest serverRequest) {
        String userId = serverRequest.pathVariable("id");
        return userService.getUserById(userId)
                .flatMap(user -> userService.deleteUser(user)
                        .then(ServerResponse.ok().build()))
                .switchIfEmpty(ServerResponse.notFound().build());
    }
}
