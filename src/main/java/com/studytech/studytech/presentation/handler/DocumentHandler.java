package com.studytech.studytech.presentation.handler;

import com.studytech.studytech.util.document.processes.FileUploadProcessor;
import com.studytech.studytech.util.document.processes.QuestionGenerationProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class DocumentHandler {

    private final FileUploadProcessor fileUploadProcessor;
    private final QuestionGenerationProcessor questionGenerationProcessor;
    private final ResponseBuilder responseBuilder;

    public Mono<ServerResponse> showUploadForm(ServerRequest request) {
        log.debug("Showing upload form");
        return responseBuilder.buildSuccessResponse();
    }

    public Mono<ServerResponse> uploadMarkdownFile(ServerRequest request) {
        log.info("Processing markdown file upload");

        return request.multipartData()
                .flatMap(multipartData -> fileUploadProcessor.processFileUpload(
                        multipartData,
                        questionGenerationProcessor::generateQuestions
                ));
    }

    public Mono<ServerResponse> generateQuestions(String markdownContent) {
        log.info("Direct question generation requested");
        return questionGenerationProcessor.generateQuestions(markdownContent);
    }
}
