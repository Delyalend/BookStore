package com.bookstore.service;

import com.bookstore.typeSearchForUser.TypeSearchAbstractForUser;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class TypeSearchConverterForUserImpl implements TypeSearchConverterForUser {

    @Autowired
    private ApplicationContext applicationContext;

    @SneakyThrows
    public TypeSearchAbstractForUser getTypeSearch(String classNamePart1, String classNamePart2) {

        if(classNamePart2.equals("All")) {

            TypeSearchAbstractForUser typeSearch = (TypeSearchAbstractForUser)Class.forName("com.bookstore.typeSearchForUser." + classNamePart1 + "TypeSearchForUser").getDeclaredConstructor().newInstance();
            AutowireCapableBeanFactory factory = applicationContext.getAutowireCapableBeanFactory();
            factory.autowireBean(typeSearch);
            factory.initializeBean(typeSearch, "com.bookstore.typeSearchForUser." + classNamePart1 + "TypeSearchForUser");

            return typeSearch;
        }
        else {

            TypeSearchAbstractForUser typeSearch = (TypeSearchAbstractForUser) Class.forName("com.bookstore.typeSearchForUser." + classNamePart1 + classNamePart2 + "TypeSearchForUser").getDeclaredConstructor().newInstance();
            AutowireCapableBeanFactory factory = applicationContext.getAutowireCapableBeanFactory();
            factory.autowireBean(typeSearch);
            factory.initializeBean(typeSearch, "com.bookstore.typeSearchForUser." + classNamePart1 + classNamePart2 + "TypeSearchForUser");

            return typeSearch;
        }


    }


}
