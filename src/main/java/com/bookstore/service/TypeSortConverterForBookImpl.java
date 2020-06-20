package com.bookstore.service;

import com.bookstore.typeSortForBook.TypeSortAbstractForBook;
import com.bookstore.typeSortForBook.TypeSortWithThemeAbstractForBook;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class TypeSortConverterForBookImpl implements TypeSortConverterForBook {

    @Autowired
    private ApplicationContext applicationContext;


    @Override
    @SneakyThrows
    public TypeSortAbstractForBook getTypeSearch(String subjectSort, String theme) {

        if(theme.equals("Все тематики")) {
            TypeSortAbstractForBook typeSort = (TypeSortAbstractForBook) Class.forName("com.bookstore.typeSortForBook." + subjectSort + "TypeSortForBook").getDeclaredConstructor().newInstance();

            AutowireCapableBeanFactory factory = applicationContext.getAutowireCapableBeanFactory();
            factory.autowireBean(typeSort);
            factory.initializeBean( typeSort, "com.bookstore.typeSortForBook." + subjectSort + "TypeSortForBook");

            return typeSort;
        }
        else {
            TypeSortWithThemeAbstractForBook typeSort = (TypeSortWithThemeAbstractForBook) Class.forName("com.bookstore.typeSortForBook." + subjectSort + "WithThemeTypeSortForBook").getDeclaredConstructor().newInstance();
            typeSort.SetTheme(theme);
            AutowireCapableBeanFactory factory = applicationContext.getAutowireCapableBeanFactory();
            factory.autowireBean(typeSort);
            factory.initializeBean( typeSort, "com.bookstore.typeSortForBook." + subjectSort + "WithThemeTypeSortForBook");

            return typeSort;
        }

    }
}
