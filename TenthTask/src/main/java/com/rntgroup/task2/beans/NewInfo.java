package com.rntgroup.task2.beans;

import org.springframework.beans.factory.support.MethodReplacer;

import java.lang.reflect.Method;

public class NewInfo implements MethodReplacer {

    @Override
    public Object reimplement(Object o, Method method, Object[] objects) {
        System.out.println("New info.");
        return o;
    }
}
