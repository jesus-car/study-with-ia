package com.studytech.studytech.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.UUID;

@Service
public class ImageService {

    private final S3AsyncClient s3Client;
    private final String bucketName;

    public ImageService(S3AsyncClient s3Client, @Value("${aws.s3.bucket}") String bucketName) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
    }

    public Mono<String> uploadProfileImage(String userId, FilePart filePart) {
        String fileName = "profile/" + userId + "-" + UUID.randomUUID() + "-" + filePart.filename();

        return DataBufferUtils.join(filePart.content())
                .map(dataBuffer -> {
                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bytes);
                    DataBufferUtils.release(dataBuffer);
                    return bytes;
                })
                .flatMap(bytes -> {
                    PutObjectRequest objectRequest = PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(fileName)
                            .contentType(filePart.headers().getContentType().toString())
                            .build();

                    return Mono.fromFuture(s3Client.putObject(objectRequest, AsyncRequestBody.fromBytes(bytes)))
                            .map(response -> "https://" + bucketName + ".s3.amazonaws.com/" + fileName);
                });
    }


}
