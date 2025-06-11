package com.studytech.studytech.util.document.processes;

import com.studytech.studytech.util.document.FileValidationService;
import com.studytech.studytech.presentation.handler.ResponseBuilder;
import com.studytech.studytech.util.document.ValidationResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileUploadProcessor {

    private final FileValidationService fileValidationService;
    private final FileContentProcessor fileContentProcessor;
    private final ResponseBuilder responseBuilder;

    // Procesa el Filedata que recibe, validando y extrayendo su contenido.
    public Mono<ServerResponse> processFileUpload(MultiValueMap<String, Part> multipartData,
                                                  // Strategy pattern to allow different processing strategies
                                                  Function<String, Mono<ServerResponse>> contentProcessor) {

        ValidationResult<FilePart> validationResult = fileValidationService.validateMultipartFile(multipartData);

        if (!validationResult.isValid()) {
            return responseBuilder.buildErrorResponse(validationResult.getErrorMessage());
        }

        return fileContentProcessor.extractContent(validationResult.getData())
                .flatMap(this::validateAndProcessContent)
                .flatMap(contentProcessor)
                .doOnSuccess(response -> log.info("File upload processed successfully"))
                .onErrorResume(this::handleProcessingError);
    }

    private Mono<String> validateAndProcessContent(String content) {
        ValidationResult<String> contentValidation = fileContentProcessor.validateContent(content);

        if (!contentValidation.isValid()) {
            return Mono.error(new IllegalArgumentException(contentValidation.getErrorMessage()));
        }

        return Mono.just(contentValidation.getData());
    }

    private Mono<ServerResponse> handleProcessingError(Throwable error) {
        log.error("Error processing file upload", error);

        if (error instanceof IllegalArgumentException) {
            return responseBuilder.buildErrorResponse(error.getMessage());
        }

        return responseBuilder.buildErrorResponse("Error processing file: " + error.getMessage());
    }
}