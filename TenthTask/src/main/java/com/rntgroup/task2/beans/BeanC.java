package com.rntgroup.task2.beans;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class BeanC {
    private BeanD beanD;

    // lookup method
    public BeanD getBeanD() {
        return null;
    }

    @Override
    public String toString() {
        return "BeanC{" +
               "beanD=" + getBeanD() +
               '}';
    }
}
