package com.bookstore.dao;

import com.bookstore.model.Role;
import com.bookstore.model.User;

public interface UserDao {

    //CREATE
    void createUser(User user);

    //READ
    User findUserByNickname(String nickname);
    User findUserById(Long id);


    //DELETE
    void deleteUserByNickname(String nickname);
    void deleteUserById(Long id);

    //ROLES
    Role findRoleByNickname(String nickname);
    void updateRoleByNickname(String titleRole, String nickname);


}
