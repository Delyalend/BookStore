package com.bookstore.service;

import com.bookstore.typeSearchForUser.TypeSearchAbstractForUser;

public interface TypeSearchConverterForUser {
    TypeSearchAbstractForUser getTypeSearch(String classNamePart1, String classNamePart2);
}
