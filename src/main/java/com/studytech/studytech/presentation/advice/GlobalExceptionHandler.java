package com.studytech.studytech.presentation.advice;

import com.studytech.studytech.util.exception.ValidationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public Mono<ServerResponse> handleValidationException(ValidationException e) {
        return ServerResponse.badRequest()
                .bodyValue(e.getErrors());
    }
}
