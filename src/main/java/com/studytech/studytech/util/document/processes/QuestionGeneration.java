package com.studytech.studytech.util.document.processes;

import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public interface QuestionGeneration {

    public Mono<ServerResponse> generateQuestions(String markdownContent);
}
