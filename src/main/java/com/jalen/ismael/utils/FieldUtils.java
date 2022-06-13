package com.jalen.ismael.utils;

import java.lang.reflect.Field;

public class FieldUtils { 
    @SuppressWarnings(value = {"deprecation", "unchecked"})
    public static  <T> T getValue(Class<?> cls, Object targetObject, String fieldName) {
        T result;
        try {
            Field field = cls.getDeclaredField(fieldName);
            if (!field.isAccessible()) {
                field.setAccessible(true);
                result = (T) field.get(targetObject);
                field.setAccessible(false);
            } else {
                result = (T) field.get(targetObject);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException(String.format("属性获取失败: 类名 -> %s, 属性名 -> %s, 具体信息: %s.", cls.getName(), fieldName, e));
        }
        return result;
    }

    public static <T> T getValue(Object targetObject, String fieldName) {
        return getValue(targetObject.getClass(), targetObject, fieldName);
    }

    @SuppressWarnings(value = {"deprecation", "unchecked"})
    public static <T> T getValue(Class<?> targetObject, String fieldName) {
        T result;
        try {
            Field field = targetObject.getDeclaredField(fieldName);
            if (!field.isAccessible()) {
                field.setAccessible(true);
                result = (T) field.get(targetObject);
                field.setAccessible(false);
            } else {
                result = (T) field.get(targetObject);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException(String.format("属性获取失败: 类名 -> %s, 属性名 -> %s, 具体信息: %s.", targetObject.getName(), fieldName, e));
        }
        return result;
    }

    @SuppressWarnings("deprecation")
    public static void setValue(Object targetObject, Field field, Object value) {
        try {
            if (!field.isAccessible()) {
                field.setAccessible(true);
                field.set(targetObject, value);
                field.setAccessible(false);
            } else {
                field.set(targetObject, value);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException(String.format("属性设置失败: 类名 -> %s, 属性名 -> %s, 具体信息: %s.", targetObject.getClass().getName(), field.getName(), e));
        }
    }

    public static void setValue(Object targetObject, String fieldName, Object value) {
        try {
            Field field = targetObject.getClass().getDeclaredField(fieldName);
            setValue(targetObject, field, value);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            throw new RuntimeException(String.format("属性设置失败: 类名 -> %s, 属性名 -> %s, 具体信息: %s.", targetObject.getClass().getName(), fieldName, e));
        }
    }
}
