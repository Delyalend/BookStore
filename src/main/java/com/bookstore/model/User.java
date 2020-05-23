package com.bookstore.model;

import com.bookstore.dao.UserDao;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import java.sql.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User{
    @Autowired
    private UserDao userDao;

    private Long id;
    private String nickname;
    private String password;
    private Date birthday;
    private boolean enabled;
    private String firstName;
    private String middleName;
    private String lastName;
    private String gender;
    private Role role;


    public String getUsername() {
        return nickname;
    }

    public boolean isAccountNonLocked() {
        return enabled;
    }

}
