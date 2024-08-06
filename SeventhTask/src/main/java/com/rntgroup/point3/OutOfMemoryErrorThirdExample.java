package com.rntgroup.point3;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;

import java.util.UUID;

public class OutOfMemoryErrorThirdExample {

    public static void main(String[] args) {
        try {
            while (true) {
                String className = "MyClass" + UUID.randomUUID().toString().replace("-", "");

                DynamicType.Builder<Object> type = new ByteBuddy()
                        .subclass(Object.class)
                        .name(className);

                // загружает новый класс в JVM
                Class<?> clazz = type.make()
                        .load(OutOfMemoryErrorThirdExample.class.getClassLoader(),
                                ClassLoadingStrategy.Default.INJECTION)
                        .getLoaded();
            }
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
        }
    }
}

