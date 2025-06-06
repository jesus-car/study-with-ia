package com.studytech.studytech.presentation.handler;


import com.studytech.studytech.presentation.advice.ObjectValidator;
import com.studytech.studytech.presentation.dto.LoginDTO;
import com.studytech.studytech.presentation.dto.RegisterDTO;
import com.studytech.studytech.service.AuthService;
import com.studytech.studytech.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AuthHandler {

    private final UserService userService;
    private final ObjectValidator objectValidator;
    private final AuthService authService;


    public Mono<ServerResponse> login(ServerRequest request) {
        Mono<LoginDTO> loginDTO = request.bodyToMono(LoginDTO.class);
        return loginDTO.flatMap(authService::login)
                .flatMap(token -> ServerResponse.ok().bodyValue(token));
    }

    public Mono<ServerResponse> register(ServerRequest request) {

        return request.bodyToMono(RegisterDTO.class)
                .flatMap(objectValidator::validate)
                .flatMap(authService::register)
                .flatMap(user -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(user));
    }
}
