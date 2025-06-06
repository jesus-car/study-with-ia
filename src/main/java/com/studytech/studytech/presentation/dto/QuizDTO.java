package com.studytech.studytech.presentation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"statement", "questions"})
public record QuizDTO(
    @JsonProperty(required = true, value = "statement") String statement,
    @JsonProperty(required = true, value = "questions") Question[] questions
){
    @JsonPropertyOrder({"question", "options"})
    record Question(
            @JsonProperty(required = true, value = "question") String question,
            QuestionOptions options
    ) {
        @JsonPropertyOrder({"options", "answer"})
        record QuestionOptions(
                @JsonProperty(required = true, value = "options") String[] options,
                @JsonProperty(required = true, value = "answer") String answer
        ) {
        }
    }
}