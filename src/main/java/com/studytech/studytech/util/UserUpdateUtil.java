package com.studytech.studytech.util;

import com.studytech.studytech.persistence.model.UserDocument;
import com.studytech.studytech.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.FormFieldPart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserUpdateUtil {

    private final ImageService imageService;

    // 🟢 Extrae el usuario del request según el Content-Type
    public Mono<UserDocument> parseUserFromRequest(ServerRequest request, MediaType contentType) {
        if (MediaType.APPLICATION_JSON.isCompatibleWith(contentType)) {
            return request.bodyToMono(UserDocument.class);
        } else if (MediaType.MULTIPART_FORM_DATA.isCompatibleWith(contentType)) {
            return request.multipartData().map(multipart -> {
                UserDocument user = new UserDocument();
                user.setUsername(getMultipartValue(multipart, "username"));
                user.setEmail(getMultipartValue(multipart, "email"));
                user.setName(getMultipartValue(multipart, "name"));
                return user;
            });
        }
        return Mono.error(new RuntimeException("Unsupported Content-Type: " + contentType));
    }

    // 🟢 Extrae la imagen si está presente
    public Mono<FilePart> extractImageFromRequest(ServerRequest request, MediaType contentType) {
        if (!MediaType.MULTIPART_FORM_DATA.isCompatibleWith(contentType)) {
            return Mono.empty();
        }
        return request.multipartData()
                .flatMap(multipart -> Optional.ofNullable(multipart.getFirst("image"))
                        .map(part -> Mono.just((FilePart) part))
                        .orElse(Mono.empty()));
    }

    // 🟢 Fusiona datos nuevos sin perder los anteriores
    public UserDocument mergeUsers(UserDocument existingUser, UserDocument updatedUser) {
        if(updatedUser.getName() != null) {
            existingUser.setName(updatedUser.getName());
        }

        if (updatedUser.getUsername() != null) {
            existingUser.setUsername(updatedUser.getUsername());
        }
        if (updatedUser.getEmail() != null) {
            existingUser.setEmail(updatedUser.getEmail());
        }
        return existingUser;
    }


    // 🟢 Maneja la actualización de imagen si se sube una nueva
    public Mono<UserDocument> updateProfileImage(String userId, UserDocument user, Mono<FilePart> imageMono) {
        return imageMono.flatMap(file -> imageService.uploadProfileImage(userId, file)
                .map(imageUrl -> {
                    user.setProfileImageURL(imageUrl);
                    return user;
                })
        ).defaultIfEmpty(user);
    }

    // 🟢 Extrae valores de campos de texto en multipart/form-data
    private String getMultipartValue(MultiValueMap<String, Part> multipartData, String key) {
        Part part = multipartData.getFirst(key);
        if (part instanceof FormFieldPart formFieldPart) {
            return formFieldPart.value();
        }
        return null;
    }
}
