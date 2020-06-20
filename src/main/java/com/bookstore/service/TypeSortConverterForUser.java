package com.bookstore.service;

import com.bookstore.typeSortForUser.TypeSortForUser;

public interface TypeSortConverterForUser {

    TypeSortForUser getTypeSort(String className);

}
