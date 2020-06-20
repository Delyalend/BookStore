package com.bookstore.service;

import com.bookstore.typeSearchForBook.TypeSearchForBook;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class TypeSearchConverterForBookImpl implements TypeSearchConverterForBook {

    @Autowired
    private ApplicationContext applicationContext;

    @SneakyThrows
    public TypeSearchForBook getTypeSearch(String className) {

        TypeSearchForBook typeSearchForBook = (TypeSearchForBook) Class.forName("com.bookstore.typeSearchForBook." + className + "TypeSearchForBook").getDeclaredConstructor().newInstance();

        AutowireCapableBeanFactory factory = applicationContext.getAutowireCapableBeanFactory();
        factory.autowireBean(typeSearchForBook);
        factory.initializeBean(typeSearchForBook, "com.bookstore.typeSearchForBook." + className + "TypeSearchForBook");

        return typeSearchForBook;
    }
}
