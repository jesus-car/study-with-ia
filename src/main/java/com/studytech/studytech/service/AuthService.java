package com.studytech.studytech.service;

import com.studytech.studytech.persistence.model.UserDocument;
import com.studytech.studytech.persistence.repository.UserRepository;
import com.studytech.studytech.presentation.dto.LoginDTO;
import com.studytech.studytech.presentation.dto.RegisterDTO;
import com.studytech.studytech.presentation.dto.TokenDTO;
import com.studytech.studytech.presentation.dto.UserPublicDataDTO;
import com.studytech.studytech.security.JwtUtil;
import com.studytech.studytech.util.RoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public Mono<TokenDTO> login(LoginDTO loginDTO) {
        return userRepository.findByUsernameOrEmail(loginDTO.getEmail(), loginDTO.getEmail())
                .switchIfEmpty(Mono.error(new RuntimeException("Invalid credentials")))
                .filter(user -> passwordEncoder.matches(loginDTO.getPassword(), user.getPassword()))
                .map(user -> new TokenDTO(jwtUtil.generateAccessToken(user)))
                .switchIfEmpty(Mono.error(new RuntimeException("Invalid credentials")));
    }

    // 🟢 Crea un nuevo usuario
    public Mono<UserPublicDataDTO> register(RegisterDTO registerDTO) {
        return userRepository.existsByUsername(registerDTO.getUsername())
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new RuntimeException("Username already exists"));
                    }

                    return userRepository.existsByEmail(registerDTO.getEmail());
                })
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new RuntimeException("Email already exists"));
                    }

                    UserDocument user = UserDocument.builder()
                            .username(registerDTO.getUsername())
                            .email(registerDTO.getEmail())
                            .password(passwordEncoder.encode(registerDTO.getPassword()))
                            .role(RoleEnum.ROLE_ADMIN)
                            .build();

                    return userRepository.save(user);
                })
                .map(UserPublicDataDTO::new);
    }
}
