package com.bookstore.typeSearchForUser;

import com.bookstore.dao.UserDao;
import com.bookstore.model.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public abstract class TypeSearchAbstractForUser {

    @Autowired
    protected UserDao userDao;

    public abstract List<User> getUsers(String text);

}
