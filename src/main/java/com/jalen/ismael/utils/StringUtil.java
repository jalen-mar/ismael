package com.jalen.ismael.utils;

import java.util.Collection;

public class StringUtil { 
    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    public static boolean isEmpty(String value) {
        return value == null || value.trim().length() == 0;
    }

    public static boolean isNotEmpty(String value) {
        return !isEmpty(value);
    }

    public static boolean between(String value, int min, int max) {
        boolean result = false;
        if (!isEmpty(value)) {
            int len = value.length();
            if (len >= min && len <= max) {
                result = true;
            }
        }
        return result;
    }

    public static boolean isEqual(String v1, String v2) {
        if (v1 != null) {
            return v1.equals(v2);
        }
        return v2 == null;
    }

    public static String[] toStringArray(Collection<String> collection) {
        return !CollectionUtil.isEmpty(collection) ? collection.toArray(EMPTY_STRING_ARRAY) : EMPTY_STRING_ARRAY;
    }

    public static String toString(String[] array) {
        if (array == null) {
            return "";
        }
        if (array.length == 0) {
            return "[]";
        }
        int max = array.length - 1;
        StringBuilder b = new StringBuilder();
        b.append('[');
        for (int i = 0; ; i++) {
            b.append(array[i]);
            if (i == max)
                return b.append(']').toString();
            b.append(", ");
        }
    }
}
