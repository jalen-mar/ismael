package com.jalen.ismael.config;

import java.util.Map;

public class PluginConfiguration {  
    private String className;
    private Object pluginInstance;
    private Map<String, Object> data;
    private int order;
    private Object configurationObject;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }


    public Object getPluginInstance() {
        return pluginInstance;
    }

    public void setPluginInstance(Object pluginInstance) {
        this.pluginInstance = pluginInstance;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
    
    public Object getConfigurationObject() {
        return configurationObject;
    }

    public void setConfigurationObject(Object configurationObject) {
        this.configurationObject = configurationObject;
    }
}
