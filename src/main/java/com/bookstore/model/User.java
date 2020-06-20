package com.bookstore.model;

import com.bookstore.dao.UserDao;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

import java.sql.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User{


    @Autowired
    private UserDao userDao;

    private Long userId;
    private String nickname;
    private String password;
    private Date birthday;
    private boolean enabled;
    private String firstname;
    private String middlename;
    private String lastname;
    private String gender;
    private Role role;


    public String getUsername() {
        return nickname;
    }

    public boolean isAccountNonLocked() {
        return enabled;
    }


}
