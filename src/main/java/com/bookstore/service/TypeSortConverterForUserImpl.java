package com.bookstore.service;

import com.bookstore.typeSortForUser.TypeSortForUser;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class TypeSortConverterForUserImpl implements TypeSortConverterForUser {

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    @SneakyThrows
    public TypeSortForUser getTypeSort(String className) {

        TypeSortForUser typeSort = (TypeSortForUser) Class.forName("com.bookstore.typeSortForUser." + className + "TypeSortForUser").getDeclaredConstructor().newInstance();

        AutowireCapableBeanFactory factory = applicationContext.getAutowireCapableBeanFactory();
        factory.autowireBean(typeSort);
        factory.initializeBean(typeSort, "com.bookstore.typeSortForUser." + className + "TypeSortForUser");

        return typeSort;
    }
}
