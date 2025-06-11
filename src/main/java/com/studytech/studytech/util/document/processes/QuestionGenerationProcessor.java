package com.studytech.studytech.util.document.processes;

import com.studytech.studytech.service.DocumentService;
import com.studytech.studytech.presentation.handler.ResponseBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuestionGenerationProcessor implements QuestionGeneration{

    private final DocumentService documentService;
    private final ResponseBuilder responseBuilder;

    @Override
    public Mono<ServerResponse> generateQuestions(String markdownContent) {
        log.info("Generating questions from markdown content");

        return documentService.generateQuestionsFromMarkdown(markdownContent)
                .flatMap(responseBuilder::buildJsonResponse)
                .doOnSuccess(response -> log.info("Questions generated successfully"))
                .onErrorResume(this::handleGenerationError);
    }

    private Mono<ServerResponse> handleGenerationError(Throwable error) {
        log.error("Error generating questions from markdown", error);
        return responseBuilder.buildJsonErrorResponse("Error generating questions: " + error.getMessage());
    }
}