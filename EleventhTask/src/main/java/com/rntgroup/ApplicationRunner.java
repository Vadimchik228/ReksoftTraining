package com.rntgroup;

import com.rntgroup.config.ApplicationConfig;
import com.rntgroup.entity.User;
import com.rntgroup.repository.UserRepository;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Set;
import java.util.stream.Collectors;

public class ApplicationRunner {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(
                ApplicationConfig.class
        );

        UserRepository userRepository = context.getBean(UserRepository.class);

        int friendsCount = 40; // по условию надо 100
        int likesCount = 40; // по условию надо 100
        int month = 3;
        int year = 2025;

        Set<String> uniqueNames = userRepository.findAllByFriendsAndLikesAtSpecifiedPeriod(
                        friendsCount,
                        likesCount,
                        year,
                        month
                ).stream()
                .map(User::getName)
                .collect(Collectors.toSet());

        System.out.println("Имена пользователей, которые имеют более " +
                           friendsCount + " друзей и " + likesCount + " лайков в " +
                           year + " году (месяц " + month + "):");
        uniqueNames.forEach(System.out::println);
    }
}
