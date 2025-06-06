package com.studytech.studytech.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Configuration
public class AiConfig {

    @Value("classpath:/static/prompt/ExpertSystemPrompt")
    private Resource proSystemPrompt;

    @Bean
    ChatClient chatClient(ChatClient.Builder builder){
        try {
            System.out.println("Loading AI configuration with system prompt: " + proSystemPrompt.getContentAsString(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return builder.defaultSystem(proSystemPrompt)
                .defaultOptions(ChatOptions.builder()
                        .build())
                .build();
    }
}
