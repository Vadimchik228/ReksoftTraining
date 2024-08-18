package com.rntgroup.task3;

import com.rntgroup.task3.entities.User;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ThirdApplicationRunner {
    public static void main(String[] args) {
        try (var context = new ClassPathXmlApplicationContext("application.xml")) {
            var user = context.getBean("user", User.class);

            System.out.println("\n" + user.getName() + "\n");
        }
    }
}
