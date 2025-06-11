package com.studytech.studytech.config;

import com.studytech.studytech.util.document.processes.FileContentProcessor;
import com.studytech.studytech.util.document.FileValidationService;
import com.studytech.studytech.presentation.handler.ResponseBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
public class DocumentProcessingConfig {

    @Bean
    public FileValidationService fileValidationService() {
        return new FileValidationService();
    }

    @Bean
    public FileContentProcessor fileContentProcessor() {
        return new FileContentProcessor();
    }

    @Bean
    public ResponseBuilder responseBuilder() {
        return new ResponseBuilder();
    }
}