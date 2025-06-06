package com.studytech.studytech.presentation.handler;

import com.studytech.studytech.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DocumentHandler {

    private final DocumentService documentService;

    public Mono<ServerResponse> showUploadForm(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.TEXT_HTML)
                .render("upload-form");
    }

    public Mono<ServerResponse> uploadMarkdownFile(ServerRequest request) {
        return request.multipartData()
                .flatMap(multipartData -> {
                    if (!multipartData.containsKey("file")) {
                        return ServerResponse.badRequest()
                                .contentType(MediaType.TEXT_HTML)
                                .render("upload-form", Map.of("error", "No file part found in request"));
                    }

                    Part part = multipartData.getFirst("file");
                    if (!(part instanceof FilePart)) {
                        return ServerResponse.badRequest()
                                .contentType(MediaType.TEXT_HTML)
                                .render("upload-form", Map.of("error", "Invalid file upload"));
                    }

                    FilePart filePart = (FilePart) part;
                    String filename = filePart.filename();

                    if (!filename.toLowerCase().endsWith(".md")) {
                        return ServerResponse.badRequest()
                                .contentType(MediaType.TEXT_HTML)
                                .render("upload-form", Map.of("error", "Only .md files are allowed"));
                    }

                    return filePart.content()
                            .reduce(new StringBuilder(), (stringBuilder, dataBuffer) -> {
                                byte[] bytes = new byte[dataBuffer.readableByteCount()];
                                dataBuffer.read(bytes);
                                return stringBuilder.append(new String(bytes, StandardCharsets.UTF_8));
                            })
                            .flatMap(stringBuilder -> {
                                String markdownContent = stringBuilder.toString();

                                if (markdownContent.isEmpty()) {
                                    return ServerResponse.badRequest()
                                            .contentType(MediaType.TEXT_HTML)
                                            .render("upload-form", Map.of("error", "Uploaded file is empty"));
                                }

                                // Procesar el contenido con IA
                                return generateQuestions(markdownContent);
                            });
                })
                .onErrorResume(e -> {
                    e.printStackTrace(); // Log the error (replace with proper logging in production)
                    return ServerResponse.badRequest()
                            .contentType(MediaType.TEXT_HTML)
                            .render("upload-form", Map.of("error", "Error processing file: " + e.getMessage()));
                });
    }

    // Alternativa: Si quieres generar preguntas directamente desde el contenido Markdown
    public Mono<ServerResponse> generateQuestions(String markdownContent) {
        return documentService.generateQuestionsFromMarkdown(markdownContent)
                .flatMap(quizDTO -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(quizDTO))
                .onErrorResume(e -> {
                    e.printStackTrace(); // Log the error (replace with proper logging in production)
                    return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(Map.of("error", "Error generating questions: " + e.getMessage()));
                });
    }
}
