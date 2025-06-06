package com.studytech.studytech.presentation.dto;

import com.studytech.studytech.persistence.model.UserDocument;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserPublicDataDTO {
    private String id;
    private String name;
    private String username;
    private String email;
    private String profileImageURL;

    public UserPublicDataDTO(UserDocument user) {
        this.id = user.getId();
        this.name = user.getName();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.profileImageURL = user.getProfileImageURL();
    }

}
