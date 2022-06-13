package com.jalen.ismael;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.jalen.ismael.config.ComfigurationExecutor;

public class IsmaelContext {
    private static IsmaelContext instance;

    private boolean init = false;
    private Map<Class<?>, Object> components = new HashMap<>();

    public static IsmaelContext getInstance() {
        if (instance == null) {
            synchronized (IsmaelContext.class) {
                if (instance == null) {
                    instance = new IsmaelContext();
                }
            }
        }
        return instance;
    }

    private IsmaelContext() {}

    public void init(Object configurationObject) {
        if (!init) {
            synchronized (instance) {
                if (!init) {
                    new ComfigurationExecutor(configurationObject, components).start();
                    init = true;
                }
            }
        }
    }

    @SuppressWarnings(value = {"unchecked"})
    public <T> T getComponment(Class<?> targetClass) {
        Object result = components.get(targetClass);
        if (result == null) {
            synchronized (this) {
                if (result == null) {
                    Class<?> currentClass = null;
                    Iterator<Class<?>> iterator =  components.keySet().iterator();
                    while (iterator.hasNext()) {
                        currentClass = iterator.next();
                        if (targetClass.isAssignableFrom(currentClass)) {
                            break;
                        }
                        currentClass = null;
                    }
                    if (currentClass != null) {
                        result = components.get(currentClass);
                        components.put(targetClass, result);
                    }
                }
            }
        }
        return (T) components.get(targetClass);
    }
}
