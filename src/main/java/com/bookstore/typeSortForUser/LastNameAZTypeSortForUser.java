package com.bookstore.typeSortForUser;

import com.bookstore.model.User;

import java.util.List;

public class LastNameAZTypeSortForUser extends TypeSortForUser {
    @Override
    public List<User> getUsers() {
        return userDao.findAllUsersSortBySomeAZ("lastname");
    }
}
