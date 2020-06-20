package com.bookstore.typeSearchForUser;

import com.bookstore.model.User;

import java.util.List;

public class LastNameAdminTypeSearchForUser extends TypeSearchAbstractForUser {
    @Override
    public List<User> getUsers(String text) {
        return userDao.findUserByLastNameWithRole(text, "ROLE_ADMIN");
    }
}
