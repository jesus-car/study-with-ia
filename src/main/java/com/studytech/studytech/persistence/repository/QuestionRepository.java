package com.studytech.studytech.persistence.repository;

import com.studytech.studytech.persistence.model.QuestionDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface QuestionRepository extends ReactiveMongoRepository<QuestionDocument, String> {

}