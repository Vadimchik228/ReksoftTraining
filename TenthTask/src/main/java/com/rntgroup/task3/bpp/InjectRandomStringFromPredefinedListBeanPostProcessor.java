package com.rntgroup.task3.bpp;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.ReflectionUtils;

import java.util.Arrays;
import java.util.Random;

public class InjectRandomStringFromPredefinedListBeanPostProcessor implements BeanPostProcessor {
    private static final Random random = new Random();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Arrays.stream(bean.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(InjectRandomString.class))
                .forEach(field -> {
                    InjectRandomString annotation = field.getAnnotation(InjectRandomString.class);
                    String[] predefinedList = annotation.value();

                    if (predefinedList.length > 0) {
                        String randomString = predefinedList[random.nextInt(predefinedList.length)];

                        ReflectionUtils.makeAccessible(field);
                        ReflectionUtils.setField(field, bean, randomString);
                    }
                });

        return bean;
    }
}
