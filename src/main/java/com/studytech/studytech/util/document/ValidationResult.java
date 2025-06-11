package com.studytech.studytech.util.document;

public class ValidationResult<T> {
    private final boolean valid;
    private final String errorMessage;
    private final T data;

    private ValidationResult(boolean valid, String errorMessage, T data) {
        this.valid = valid;
        this.errorMessage = errorMessage;
        this.data = data;
    }

    public static <T> ValidationResult<T> success(T data) {
        return new ValidationResult<>(true, null, data);
    }

    public static <T> ValidationResult<T> error(String errorMessage) {
        return new ValidationResult<>(false, errorMessage, null);
    }

    public boolean isValid() {
        return valid;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public T getData() {
        return data;
    }
}