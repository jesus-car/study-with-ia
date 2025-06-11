package com.studytech.studytech.util.document.processes;

import com.studytech.studytech.util.document.ValidationResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Service
@Slf4j
public class FileContentProcessor {

    public Mono<String> extractContent(FilePart filePart) {
        log.debug("Extracting content from file: {}", filePart.filename());

        return filePart.content()
                .reduce(new StringBuilder(), (stringBuilder, dataBuffer) -> {
                    try {
                        byte[] bytes = new byte[dataBuffer.readableByteCount()];
                        dataBuffer.read(bytes);
                        return stringBuilder.append(new String(bytes, StandardCharsets.UTF_8));
                    } finally {
                        DataBufferUtils.release(dataBuffer);
                    }
                })
                .map(StringBuilder::toString)
                .doOnSuccess(content -> log.debug("Content extracted successfully, length: {}", content.length()))
                .doOnError(error -> log.error("Error extracting file content", error));
    }

    public ValidationResult<String> validateContent(String content) {
        if (content == null || content.trim().isEmpty()) {
            log.warn("File content is empty");
            return ValidationResult.error("Uploaded file is empty");
        }

        log.debug("Content validation successful");
        return ValidationResult.success(content);
    }
}
