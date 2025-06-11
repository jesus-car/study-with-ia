package com.studytech.studytech.persistence.repository;

import com.studytech.studytech.persistence.model.QuizDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface QuizRespository extends ReactiveMongoRepository<QuizDocument, String> {

}
