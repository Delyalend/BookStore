package com.bookstore.security;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public interface AccountValidatorService {


    public void doValidation(Authentication authentication);


}
