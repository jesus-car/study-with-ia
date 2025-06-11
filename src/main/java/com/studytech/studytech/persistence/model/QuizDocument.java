package com.studytech.studytech.persistence.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "quiz")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizDocument {
    @Id
    private String id;

    private String prompt;
    private String userId;
    private List<QuestionDocument> questions;

}