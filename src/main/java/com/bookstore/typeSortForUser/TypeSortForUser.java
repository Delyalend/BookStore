package com.bookstore.typeSortForUser;

import com.bookstore.dao.UserDao;
import com.bookstore.model.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public abstract class TypeSortForUser {

    @Autowired
    protected UserDao userDao;

    public abstract List<User> getUsers();
}
