package com.bookstore.typeSearchForUser;

import com.bookstore.model.User;

import java.util.List;

public class FirstNameUserTypeSearchForUser extends TypeSearchAbstractForUser {


    @Override
    public List<User> getUsers(String text) {
        return userDao.findUsersByFirstNameWithRole(text,"ROLE_USER");
    }
}
