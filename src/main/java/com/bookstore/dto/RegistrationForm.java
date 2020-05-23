package com.bookstore.dto;

import com.bookstore.model.Role;
import com.bookstore.model.User;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.sql.Date;

@Data
public class RegistrationForm {

    private String nickname;
    private String password;
    private String confirm;
    private Date birthday;
    private String firstname;
    private String middlename;
    private String lastname;
    private String gender;
    private String role;

    public User toUser(PasswordEncoder passwordEncoder) {

        Role userRole = new Role();
        userRole.setTitle(role);

        return User.builder()
                .nickname(nickname)
                .password(passwordEncoder.encode(password))
                .birthday(birthday)
                .firstName(firstname)
                .middleName(middlename)
                .lastName(lastname)
                .gender(gender)
                .role(userRole)
                .enabled(true)
                .build();
    }
}
