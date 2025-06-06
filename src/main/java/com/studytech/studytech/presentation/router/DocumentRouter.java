package com.studytech.studytech.presentation.router;

import com.studytech.studytech.presentation.handler.DocumentHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class DocumentRouter {
    @Bean
    public RouterFunction<ServerResponse> documentRoutes(DocumentHandler documentHandler) {
        return route()
                .GET("/documents/upload-form", documentHandler::showUploadForm)
                .POST("/documents/upload", accept(MediaType.MULTIPART_FORM_DATA), documentHandler::uploadMarkdownFile)
                .build();
    }
}