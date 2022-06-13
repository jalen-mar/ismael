package com.jalen.ismael.utils;

import java.util.Collection;

public class CollectionUtil { 
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isEmpty(Object[] array) {
        return array == null || array.length == 0;
    }
}
