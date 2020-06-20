package com.bookstore.dao;

import com.bookstore.model.Role;
import com.bookstore.model.User;

import java.util.List;

public interface UserDao {

    //CREATE
    void createUser(User user);

    //READ
    User findUserByNickname(String nickname);
    User findUserById(Long id);
    User findUserBySaleId(Long saleId);
    List<User> findUsersByFirstName(String firstName);
    List<User> findUsersByMiddleName(String middleName);
    List<User> findUserByLastName(String lastName);
    List<User> findUsersByFirstNameWithRole(String firstName, String roleTitle);
    List<User> findUsersByMiddleNameWithRole(String middleName, String roleTitle);
    List<User> findUserByLastNameWithRole(String lastName, String roleTitle);
    List<User> findAllUsersSortBySomeAZ(String some);
    List<User> findAllUsersSortBySomeZA(String some);

    //DELETE
    void deleteUserById(Long id);

    //BAN
    void banUserById(Long id);
    void unbanUserById(Long id);
    boolean isUserValidById(String nickname);

    //ROLES
    Role findRoleByNickname(String nickname);
    void updateRoleByNickname(String titleRole, String nickname);

    //UPDATE
    void updateUserColumnById(String titleColumn,String text, Long userId);

}
