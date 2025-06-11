package com.studytech.studytech.util.document;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.util.Arrays;

@Service
@Slf4j
public class FileValidationService {

    private static final String[] ALLOWED_EXTENSIONS = {".md"};
    private static final String FILE_PART_KEY = "file";

    public ValidationResult<FilePart> validateMultipartFile(MultiValueMap<String, Part> multipartData) {
        log.debug("Validating multipart file upload");

        if (!multipartData.containsKey(FILE_PART_KEY)) {
            log.warn("No file part found in request");
            return ValidationResult.error("No file part found in request");
        }

        Part part = multipartData.getFirst(FILE_PART_KEY);

        if (!(part instanceof FilePart)) {
            log.warn("Invalid file upload - not a FilePart instance");
            return ValidationResult.error("Invalid file upload");
        }

        FilePart filePart = (FilePart) part;

        ValidationResult<Void> extensionValidation = validateFileExtension(filePart.filename());
        if (!extensionValidation.isValid()) {
            return ValidationResult.error(extensionValidation.getErrorMessage());
        }

        log.debug("File validation successful for: {}", filePart.filename());
        return ValidationResult.success(filePart);
    }

    private ValidationResult<Void> validateFileExtension(String filename) {
        if (filename == null || filename.trim().isEmpty()) {
            log.warn("Filename is null or empty");
            return ValidationResult.error("Filename cannot be empty");
        }

        String lowercaseFilename = filename.toLowerCase();
        boolean isValidExtension = Arrays.stream(ALLOWED_EXTENSIONS)
                .anyMatch(lowercaseFilename::endsWith);

        if (!isValidExtension) {
            log.warn("Invalid file extension for file: {}", filename);
            String allowedExtensionsStr = String.join(", ", ALLOWED_EXTENSIONS);
            return ValidationResult.error("Only " + allowedExtensionsStr + " files are allowed");
        }

        return ValidationResult.success(null);
    }
}