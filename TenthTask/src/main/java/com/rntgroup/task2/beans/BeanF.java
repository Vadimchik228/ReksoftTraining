package com.rntgroup.task2.beans;

import lombok.Getter;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

@Getter
public class BeanF implements InitializingBean, DisposableBean {
    private String value;

    public BeanF() {
        System.out.println("Вызвался конструктор у beanF.");
    }

    public void setValue(String value) {
        System.out.println("Вызывался сеттер у beanF.");
        this.value = value;
    }

    @Override
    public void afterPropertiesSet() {
        System.out.println("Вызвался afterPropertiesSet-метод у beanF.");
    }

    @Override
    public void destroy() {
        System.out.println("Вызвался destroy-метод (DisposableBean) у beanF.");
    }

    public void init() {
        System.out.println("Вызвался init-метод у beanF.");
    }

    public void simpleDestroy() {
        System.out.println("Вызвался simpleDestroy-метод у beanF.");
    }
}
