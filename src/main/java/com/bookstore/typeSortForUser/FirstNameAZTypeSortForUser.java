package com.bookstore.typeSortForUser;

import com.bookstore.model.User;

import java.util.List;

public class FirstNameAZTypeSortForUser extends TypeSortForUser {
    @Override
    public List<User> getUsers() {
        return userDao.findAllUsersSortBySomeAZ("firstname");
    }
}
