package com.jalen.ismael.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ReflectUtils { 
    @SuppressWarnings("unchecked")
    public static <T> Class<T> getClass(String className) {
        Class<?> result;
        try {
            result = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(String.format("当前类找不到: 类名 -> %s.", className));
        }
        return (Class<T>) result;
    }

    @SuppressWarnings("unchecked")
    public static <T> T newInstance(Class<T> cls, Object... params) {
        try {
            int paramsCount = params.length;
            if (paramsCount == 0) {
                return cls.getDeclaredConstructor().newInstance();
            } else {
                Constructor<?>[] constructors = cls.getDeclaredConstructors();
                for (Constructor<?> constructor : constructors) {
                    Class<?>[] types = constructor.getParameterTypes();
                    if (paramsCount == types.length) {
                        Constructor<?> targetConstructor = constructor;
                        for (int i = 0; i < paramsCount; i++) {
                            if (!types[i].isAssignableFrom(params[i].getClass())) {
                                targetConstructor = null;
                                break;
                            }
                        }
                        if (targetConstructor != null) {
                            return (T) targetConstructor.newInstance(params);
                        }
                    }
                }
                throw new RuntimeException(String.format("当前类初始化失败: 类名 -> %s,未找到构造方法.", cls.getName()));
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
            throw new RuntimeException(String.format("当前类初始化失败: 类名 -> %s.", cls.getName()));
        }
    }

    public static <T> T newInstance(String className, Object... params) {
        return newInstance(getClass(className), params);
    }

    public static Constructor<?> getConstructor(Class<?> targetClass, Class<? extends Annotation> annotationClass) {
        Constructor<?>[] constructors = targetClass.getConstructors();
        for (Constructor<?> constructor : constructors) {
            if (AnnotationUtils.hasAnnotation(constructor, annotationClass)) {
                return constructor;
            }
        }
        return null;
    }
}
