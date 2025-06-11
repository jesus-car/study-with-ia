package com.studytech.studytech.presentation.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class ResponseBuilder {

    public Mono<ServerResponse> buildErrorResponse(String errorMessage) {
        return ServerResponse.badRequest()
                .contentType(MediaType.TEXT_HTML)
                .render("upload-form", Map.of("error", errorMessage));
    }

    public Mono<ServerResponse> buildSuccessResponse() {
        return ServerResponse.ok()
                .contentType(MediaType.TEXT_HTML)
                .render("upload-form");
    }

    public Mono<ServerResponse> buildJsonResponse(Object data) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(data);
    }

    public Mono<ServerResponse> buildJsonErrorResponse(String errorMessage) {
        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of("error", errorMessage));
    }
}