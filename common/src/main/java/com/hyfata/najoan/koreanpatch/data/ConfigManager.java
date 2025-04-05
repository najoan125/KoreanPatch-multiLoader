package com.hyfata.najoan.koreanpatch.data;

import com.hyfata.najoan.koreanpatch.data.config.ModConfig;

public class ConfigManager {
    private static ModConfig config = new ModConfig();

    public static ModConfig getConfig() {
        return config;
    }

    public static void setConfig(ModConfig config) {
        ConfigManager.config = config;
    }
}
