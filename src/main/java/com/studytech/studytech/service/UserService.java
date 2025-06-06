package com.studytech.studytech.service;

import com.studytech.studytech.persistence.model.UserDocument;
import com.studytech.studytech.persistence.repository.UserRepository;
import com.studytech.studytech.util.UserUpdateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserUpdateUtil userUtil;

    public Flux<UserDocument> getAllUsers() {
        return userRepository.findAll();
    }

    public Mono<UserDocument> updateUser(String userId, ServerRequest serverRequest) {
        MediaType contentType = serverRequest.headers().contentType().orElse(MediaType.APPLICATION_JSON);

        Mono<UserDocument> userRequestUpdate = userUtil.parseUserFromRequest(serverRequest, contentType);
        Mono<FilePart> imageMono = userUtil.extractImageFromRequest(serverRequest, contentType);

        return userRepository.findById(userId)
                .switchIfEmpty(Mono.error(new RuntimeException("User not found")))
                .flatMap(existingUser -> userRequestUpdate.map(updatedUser -> userUtil.mergeUsers(existingUser, updatedUser)))
                .flatMap(updatedUser -> userUtil.updateProfileImage(userId, updatedUser, imageMono))
                .flatMap(userRepository::save);
    }

    public Mono<UserDocument> getUserById(String userId) {
        return userRepository.findById(userId);
    }

    public Mono<Void> deleteUser(UserDocument user) {
        return userRepository.delete(user);
    }
}
