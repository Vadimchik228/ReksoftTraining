package com.rntgroup.task2;

import com.rntgroup.task2.beans.*;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SecondApplicationRunner {
    public static void main(String[] args) {
        try (var context = new ClassPathXmlApplicationContext("application.xml")) {
            var beanA = context.getBean("beanA", BeanA.class);
            System.out.println("\n" + beanA + "\n");

            var beanB = context.getBean("beanB", BeanB.class);
            System.out.println(beanB + "\n");

            var beanC = context.getBean("beanC", BeanC.class);
            System.out.println(beanC + "\n");

            var beanE = context.getBean("beanE", BeanE.class);
            beanE.printInfo();
            System.out.println();

            var beanF = context.getBean("beanF", BeanF.class);
            System.out.println("Конец работы SecondApplicationRunner.");
        }
    }
}
