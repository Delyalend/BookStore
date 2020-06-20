package com.bookstore.security;

import com.bookstore.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class AccountValidatorServiceImpl implements AccountValidatorService {

    @Autowired
    private UserDao userDao;

    @Override
    public void doValidation(Authentication authentication) {
        if (!userDao.isUserValidById(authentication.getName())) {
            authentication.setAuthenticated(false);
        }
    }
}
