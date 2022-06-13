package com.jalen.ismael.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class MethodUtils { 
    public static <T> T invoke(Object targetObject, String methodName, Map<Class<?>, Object> parameter) {
        Class<?>[] types = null;
        Object[] args = null;
        if (parameter != null) {
            types = parameter.keySet().toArray(new Class<?>[parameter.size()]);
            args = parameter.values().toArray(new Object[parameter.size()]);
        } else {
            types = new Class<?>[0];
            args = new Object[0];
        }
        return invoke(targetObject, getMethod(targetObject, methodName, types), args);
    }

    @SuppressWarnings(value = {"deprecation", "unchecked"})
    public static <T> T invoke(Object targetObject, Method method, Object... args) {
        try {
            Object result;
            if (!method.isAccessible()) {
                method.setAccessible(true);
                result = method.invoke(targetObject, args);
                method.setAccessible(false);
            } else {
                result = method.invoke(targetObject, args);
            }
            return (T) result;
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            throw new RuntimeException(String.format("方法执行异常: 类名 -> %s, 方法名 -> %s, 具体信息: %s.", targetObject.getClass().getName(), method.getName(), e));
        }
    }

    public static Method getMethod(Object targetObject, String methodName, Class<?>... types) {
        try {
            Class<?> objectClass = targetObject.getClass();
            try {
                return objectClass.getMethod(methodName, types);
            } catch (NoSuchMethodException e) {
                return objectClass.getDeclaredMethod(methodName, types);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            throw new RuntimeException(String.format("当前方法找不到: 类名 -> %s, 方法名 -> %s, 具体信息: %s.", targetObject.getClass().getName(), methodName, e));
        }
    }

    public static Method getMethod(Class<?> targetClass, Class<? extends Annotation> annotationClass) {
        Method[] methods = targetClass.getDeclaredMethods();
        for (Method method : methods) {
            if (AnnotationUtils.hasAnnotation(method, annotationClass)) {
                return method;
            }
        }
        return null;
    }

    public static <T> T invokeAnnotation(Annotation annotation, String methodName, Class<? extends Annotation> parentAnnotationClass) {
        try {
            return invoke(annotation, methodName, null);
        } catch (Exception e) {
            Annotation parentAnnotation = AnnotationUtils.getAnnotation(annotation.annotationType(), parentAnnotationClass);
            return invoke(parentAnnotation, methodName, null);
        }
    }
}
