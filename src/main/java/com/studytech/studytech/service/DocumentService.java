package com.studytech.studytech.service;

import com.studytech.studytech.presentation.dto.QuizDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final ChatClient chatClient;

    @Value("classpath:/static/prompt/GenerateQuizPrompt")
    private Resource generateQuizPrompt;

    public Mono<QuizDTO> generateQuestionsFromMarkdown(String markdownContent) {

        var converter = new BeanOutputConverter<>(new ParameterizedTypeReference<QuizDTO>(){});

        Flux<String> response = chatClient.prompt()
                .user(u -> u.text(generateQuizPrompt)
                        .param( "format", converter.getFormat())
                        .param("document", markdownContent)
                        .param("q", "5") // Number of questions to generate
                        .param("o", "5") // Number of options per question
                )
                .stream()
                .content();

        return response.collectList()
                .map(list -> String.join("", list))
                .map(converter::convert);
    }
}
