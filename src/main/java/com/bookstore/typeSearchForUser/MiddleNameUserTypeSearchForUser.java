package com.bookstore.typeSearchForUser;

import com.bookstore.model.User;

import java.util.List;

public class MiddleNameUserTypeSearchForUser extends TypeSearchAbstractForUser {

    @Override
    public List<User> getUsers(String text) {
        return userDao.findUsersByMiddleNameWithRole(text, "ROLE_USER");
    }
}
