package com.hyfata.najoan.koreanpatch.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hyfata.najoan.koreanpatch.client.Constants;
import com.hyfata.najoan.koreanpatch.data.config.ModConfig;
import com.hyfata.najoan.koreanpatch.data.gson.ColorAdapter;
import com.hyfata.najoan.koreanpatch.data.gson.EasingFunctionsAdapter;
import com.hyfata.najoan.koreanpatch.data.gson.OutlineTypeAdapter;
import com.hyfata.najoan.koreanpatch.data.provider.EasingFunctions;
import com.hyfata.najoan.koreanpatch.data.provider.OutlineType;
import net.minecraft.client.Minecraft;

import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ConfigManager {
    private static ModConfig config = new ModConfig();
    private static final String CONFIG_FILE_NAME = Constants.MOD_ID + ".json";
    private static File CONFIG_FILE;

    private static Gson createGson() {
        return new GsonBuilder()
                .registerTypeAdapter(Color.class, new ColorAdapter())
                .registerTypeAdapter(EasingFunctions.class, new EasingFunctionsAdapter())
                .registerTypeAdapter(OutlineType.class, new OutlineTypeAdapter())
                .setPrettyPrinting()
                .create();
    }

    public static void init() {
        CONFIG_FILE = Minecraft.getInstance().gameDirectory.toPath()
                .resolve("config").resolve(CONFIG_FILE_NAME).toFile();

        if (CONFIG_FILE.exists()) {
            loadFromFile();
        }
        saveConfig(config);
    }

    private static void loadFromFile() {
        Gson gson = createGson();
        try (FileReader reader = new FileReader(CONFIG_FILE)) {
            config = gson.fromJson(reader, ModConfig.class);
        } catch (IOException e) {
            Constants.LOG.error("Failed to read config file: {}", CONFIG_FILE.toString(), e);
        }
    }

    public static ModConfig getConfig() {
        return config;
    }

    public static boolean saveConfig(ModConfig config) {
        ConfigManager.config = config;

        Gson gson = createGson();
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            gson.toJson(config, writer);
        } catch (IOException e) {
            Constants.LOG.error("Failed to write config file: {}", CONFIG_FILE.toString(), e);
            return false;
        }
        return true;
    }
}
