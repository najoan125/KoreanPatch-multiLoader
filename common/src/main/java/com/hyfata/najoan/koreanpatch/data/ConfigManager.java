package com.hyfata.najoan.koreanpatch.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hyfata.najoan.koreanpatch.client.Constants;
import com.hyfata.najoan.koreanpatch.data.config.ModConfig;
import com.hyfata.najoan.koreanpatch.data.gson.JsonCommentProcessor;
import com.hyfata.najoan.koreanpatch.data.gson.adapter.ColorAdapter;
import com.hyfata.najoan.koreanpatch.data.gson.adapter.EasingFunctionsAdapter;
import com.hyfata.najoan.koreanpatch.data.gson.adapter.OutlineTypeAdapter;
import com.hyfata.najoan.koreanpatch.data.provider.EasingFunctions;
import com.hyfata.najoan.koreanpatch.data.provider.OutlineType;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;

import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ConfigManager {
    private static ConfigManager instance;

    public static ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }

    private static final String CONFIG_FILE_NAME = Constants.MOD_ID + ".json5";
    private ModConfig config = new ModConfig();
    private File CONFIG_FILE;

    private Gson createGson() {
        return new GsonBuilder()
                .registerTypeAdapter(Color.class, new ColorAdapter())
                .registerTypeAdapter(EasingFunctions.class, new EasingFunctionsAdapter())
                .registerTypeAdapter(OutlineType.class, new OutlineTypeAdapter())
                .setPrettyPrinting()
                .create();
    }

    public void init() {
        CONFIG_FILE = Minecraft.getInstance().gameDirectory.toPath()
                .resolve("config").resolve(CONFIG_FILE_NAME).toFile();

        if (!CONFIG_FILE.getParentFile().exists()) {
            boolean ignored = CONFIG_FILE.getParentFile().mkdirs();
        }

        if (CONFIG_FILE.exists()) {
            loadFromFile();
        }
        saveConfig(config);
    }

    private void loadFromFile() {
        try (FileReader reader = new FileReader(CONFIG_FILE)) {
            JsonCommentProcessor processor = new JsonCommentProcessor(createGson());
            config = processor.readWithoutComments(reader, ModConfig.class);
        } catch (IOException e) {
            Constants.LOG.error("Failed to read config file: {}", CONFIG_FILE.toString(), e);
        }
    }

    public ModConfig getConfig() {
        return config;
    }

    public void saveConfig(ModConfig config) {
        this.config = config;

        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            JsonCommentProcessor commentedWriter = new JsonCommentProcessor(createGson());
            commentedWriter.writeWithComments(config, writer);
        } catch (IOException e) {
            Constants.LOG.error("Failed to write config file: {}", CONFIG_FILE.toString(), e);
        }
    }

    public void openConfigFile() {
        Util.getPlatform().openFile(CONFIG_FILE);
    }
}
