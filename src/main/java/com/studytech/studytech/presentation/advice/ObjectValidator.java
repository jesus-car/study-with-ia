package com.studytech.studytech.presentation.advice;

import com.studytech.studytech.util.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ObjectValidator {
    private final Validator validator;

    public <T> Mono<T> validate(T request) {
        Errors errors = new BeanPropertyBindingResult(request, request.getClass().getSimpleName() );
        validator.validate(request, errors);

        return errors.hasErrors()
                ? Mono.error(new ValidationException(errors))
                : Mono.just(request);
    }
}
