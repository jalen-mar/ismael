package com.jalen.ismael.utils;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AnnotationUtils { 
    public static List<Annotation> getAnnotations(Object targetObject, Class<? extends Annotation> annotationClass) {
        List<Annotation> result = new ArrayList<>();
        Annotation[] annotations = MethodUtils.invoke(targetObject, "getAnnotations", null);
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().isAnnotationPresent(annotationClass)) {
                result.add(annotation);
            }
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public static <T extends Annotation> T getAnnotation(Object targetObject, Class<? extends Annotation> annotationClass) {
        Annotation result = MethodUtils.invoke(targetObject, "getAnnotation", Map.of(annotationClass.getClass(), annotationClass));
        if (result == null) {
            List<Annotation> list = getAnnotations(targetObject, annotationClass);
            if (list.size() > 0) {
                result = list.get(0);
            }
        }
        return (T) result;
    }

    public static boolean hasAnnotation(Object targetObject, Class<? extends Annotation> annotationClass) {
        return getAnnotation(targetObject, annotationClass) != null;
    }
}
