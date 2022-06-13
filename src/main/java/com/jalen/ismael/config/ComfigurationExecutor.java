package com.jalen.ismael.config;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.jalen.ismael.annotation.ConfigurationProvider;
import com.jalen.ismael.annotation.EnablePlugin;
import com.jalen.ismael.annotation.InitializationCallback;
import com.jalen.ismael.annotation.IsmaelPlugin;
import com.jalen.ismael.annotation.PluginCompleted;
import com.jalen.ismael.annotation.PluginExecuted;
import com.jalen.ismael.annotation.PluginPrepared;
import com.jalen.ismael.utils.AnnotationUtils;
import com.jalen.ismael.utils.MethodUtils;
import com.jalen.ismael.utils.ReflectUtils;

public class ComfigurationExecutor extends Thread {  
    private Object configurationObject;
    private Map<Class<?>, Object> components;

    public ComfigurationExecutor(Object configurationObject, Map<Class<?>, Object> components) {
        this.configurationObject = configurationObject;
        this.components = components;
    }

    @Override
    public void run() {
        List<PluginConfiguration> config = resoluteDefaultConfig();
        if (!config.isEmpty()) {
            setupPluginConfiguration(config);
        }
        config = resoluteConfig();
        if (!config.isEmpty()) {
            setupPluginConfiguration(config);
        }
        Method method = MethodUtils.getMethod(configurationObject.getClass(), InitializationCallback.class);
        if (method != null) {
            MethodUtils.invoke(configurationObject, method);
        }

    }

    private List<PluginConfiguration> resoluteDefaultConfig() {
        Class<?> configurationClass = configurationObject.getClass();
        List<Annotation> annotations = AnnotationUtils.getAnnotations(configurationClass, EnablePlugin.class);
        List<PluginConfiguration> result = new LinkedList<>();
        for (Annotation annotation : annotations) {
            PluginConfiguration pluginConfig = new PluginConfiguration();
            pluginConfig.setConfigurationObject(configurationObject);
            pluginConfig.setClassName(MethodUtils.invokeAnnotation(annotation, "className", EnablePlugin.class));
            pluginConfig.setOrder(MethodUtils.invokeAnnotation(annotation, "order", EnablePlugin.class));
            if (!annotation.annotationType().equals(EnablePlugin.class)) {
                Method[] methods = annotation.annotationType().getDeclaredMethods();
                Map<String, Object> data = new HashMap<>();
                for (Method method : methods) {
                    data.put(method.getName(), MethodUtils.invoke(annotation, method));
                }
                pluginConfig.setData(data);
            }
            result.add(pluginConfig);
        }
        Collections.sort(result, new PluginConfigurationComparator(true));
        return result;
    }

    private List<PluginConfiguration> resoluteConfig() {
        Object theConfigurationObject = configurationObject;
        Class<?> configurationClass = theConfigurationObject.getClass();
        Method preMethod = MethodUtils.getMethod(configurationClass, ConfigurationProvider.class);
        List<PluginConfiguration> result = new LinkedList<>();
        if (preMethod != null) {
            Object preResult = MethodUtils.invoke(theConfigurationObject, preMethod);
            if (preResult != null) {
                theConfigurationObject = preResult;
                configurationClass = preResult.getClass();
            }
            List<Annotation> annotations = AnnotationUtils.getAnnotations(configurationClass, EnablePlugin.class);
            for (Annotation annotation : annotations) {
                PluginConfiguration pluginConfig = new PluginConfiguration();
                pluginConfig.setConfigurationObject(theConfigurationObject);
                pluginConfig.setClassName(MethodUtils.invokeAnnotation(annotation, "className", EnablePlugin.class));
                pluginConfig.setOrder(MethodUtils.invokeAnnotation(annotation, "order", EnablePlugin.class));
                if (!annotation.annotationType().equals(EnablePlugin.class)) {
                    Method[] methods = annotation.annotationType().getDeclaredMethods();
                    Map<String, Object> data = new HashMap<>();
                    for (Method method : methods) {
                        data.put(method.getName(), MethodUtils.invoke(annotation, method));
                    }
                    pluginConfig.setData(data);
                }
                result.add(pluginConfig);
            }
            Collections.sort(result, new PluginConfigurationComparator(true));
        }
        return result;
    }

    

    private void setupPluginConfiguration(List<PluginConfiguration> configurations) {
        Iterator<PluginConfiguration> iterator = configurations.iterator();
        while (iterator.hasNext()) {
            PluginConfiguration configuration = iterator.next();
            Object preparedResult = preparedConfiguration(configuration);
            Object executedresult = executedConfiguration(configuration, preparedResult);
            if (executedresult != null) {
                components.put(executedresult.getClass(), executedresult);
            }
            completedConfiguration(configuration, executedresult);
        }
    }

    private Object preparedConfiguration(PluginConfiguration configuration) {
        Class<?> pluginClass = ReflectUtils.getClass(configuration.getClassName());
        if (AnnotationUtils.hasAnnotation(pluginClass, IsmaelPlugin.class)) {
            Object pluginInstance = ReflectUtils.newInstance(pluginClass);
            configuration.setPluginInstance(pluginInstance);
            Method method = MethodUtils.getMethod(pluginClass, PluginPrepared.class);
            if (method != null) {
                Map<String, Object> params = configuration.getData();
                if (params != null && params.size() != 0) {
                    return MethodUtils.invoke(pluginInstance, method, configuration.getData());
                } else {
                    return MethodUtils.invoke(pluginInstance, method);
                }
            }
            return null;
        } else {
            throw new RuntimeException(String.format("当前初始化的类 %s 无@IsmaelPlugin，请检查插件配置", pluginClass.getName()));
        }
    }

    private Object executedConfiguration(PluginConfiguration configuration, Object preparedResult) {
        Class<?> pluginClass = ReflectUtils.getClass(configuration.getClassName());
        Method method = MethodUtils.getMethod(pluginClass, PluginExecuted.class);
        if (method != null) {
            if (preparedResult != null) {
                return MethodUtils.invoke(configuration.getPluginInstance(), method, preparedResult);
            } else {
                return MethodUtils.invoke(configuration.getPluginInstance(), method);
            }
        }
        return null;
    }

    private void completedConfiguration(PluginConfiguration configuration, Object executedresult) {
        Class<?> pluginClass = ReflectUtils.getClass(configuration.getClassName());
        Method method = MethodUtils.getMethod(pluginClass, PluginCompleted.class);
        if (method != null) {
            if (executedresult != null) {
                MethodUtils.invoke(configuration.getPluginInstance(), method, configuration.getConfigurationObject(), executedresult);
            } else {
                MethodUtils.invoke(configuration.getPluginInstance(), method, configuration.getConfigurationObject());
            }
        }
    }
}
