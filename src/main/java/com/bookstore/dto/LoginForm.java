package com.bookstore.dto;

import com.bookstore.model.User;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data
public class LoginForm {

    private String nickname;
    private String password;

    public User toUser(PasswordEncoder passwordEncoder) {
        return User.builder()
                .nickname(nickname)
                .password(passwordEncoder.encode(password))
                .build();
    }
}
