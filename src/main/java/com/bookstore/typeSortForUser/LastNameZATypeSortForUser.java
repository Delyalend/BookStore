package com.bookstore.typeSortForUser;

import com.bookstore.model.User;

import java.util.List;

public class LastNameZATypeSortForUser extends TypeSortForUser {
    @Override
    public List<User> getUsers() {
        return userDao.findAllUsersSortBySomeZA("lastname");
    }
}
