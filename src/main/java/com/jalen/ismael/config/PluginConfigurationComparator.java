package com.jalen.ismael.config;

import java.util.Comparator;

public class PluginConfigurationComparator implements Comparator<PluginConfiguration> { 
    private final boolean desc;

    public PluginConfigurationComparator(boolean desc) {
        this.desc = desc;
    }

    @Override
    public int compare(PluginConfiguration o1, PluginConfiguration o2) {
        return desc ? (o1.getOrder() - o2.getOrder()) : (o2.getOrder() - o1.getOrder());
    }
}
