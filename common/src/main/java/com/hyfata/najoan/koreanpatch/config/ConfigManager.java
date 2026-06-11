package com.hyfata.najoan.koreanpatch.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hyfata.najoan.koreanpatch.client.Constants;
import com.hyfata.najoan.koreanpatch.config.category.CategoryKeyBindings;
import com.hyfata.najoan.koreanpatch.config.gson.JsonCommentProcessor;
import com.hyfata.najoan.koreanpatch.config.gson.adapter.ColorAdapter;
import com.hyfata.najoan.koreanpatch.config.gson.adapter.EasingFunctionsAdapter;
import com.hyfata.najoan.koreanpatch.config.gson.adapter.OutlineTypeAdapter;
import com.hyfata.najoan.koreanpatch.keybinding.KeyIdentifier;
import com.sun.jna.Platform;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        initializeKeyBindingDefaults();
        saveConfig();
    }

    private void initializeKeyBindingDefaults() {
        CategoryKeyBindings keyBindings = config.getCategoryKeyBindings();

        if (keyBindings.getLangTypeKeys().isEmpty()) {
            List<KeyIdentifier> defaultLangKey = new ArrayList<>();
            if (Platform.isWindows()) {
                defaultLangKey.add(KeyIdentifier.fromKeyCode(GLFW.GLFW_KEY_RIGHT_ALT));
            } else if (Platform.isMac()) {
                defaultLangKey.add(KeyIdentifier.fromKeyCode(GLFW.GLFW_KEY_CAPS_LOCK));
            } else {
                defaultLangKey.add(KeyIdentifier.fromKeyCode(GLFW.GLFW_KEY_LEFT_CONTROL));
            }
            keyBindings.addLangTypeKey(defaultLangKey);
        }

        if (keyBindings.getImeKeys().isEmpty()) {
            List<KeyIdentifier> defaultImeKey = new ArrayList<>();
            defaultImeKey.add(KeyIdentifier.fromKeyCode(GLFW.GLFW_KEY_LEFT_CONTROL));
            defaultImeKey.add(KeyIdentifier.fromKeyCode(GLFW.GLFW_KEY_I));
            keyBindings.addImeKey(defaultImeKey);
        }
    }

    private void loadFromFile() {
        try (FileReader reader = new FileReader(CONFIG_FILE)) {
            JsonCommentProcessor processor = new JsonCommentProcessor(createGson());
            config = processor.readWithoutComments(reader, ModConfig.class);
        } catch (IOException e) {
            Constants.LOG.error("Failed to read config file: {}", CONFIG_FILE.toString(), e);
        }
    }

    /**
     * Reload config from file, discarding any unsaved changes
     */
    public void reloadConfig() {
        if (CONFIG_FILE != null && CONFIG_FILE.exists()) {
            loadFromFile();
        }
    }

    public ModConfig getConfig() {
        return config;
    }

    public void saveConfig() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            JsonCommentProcessor commentedWriter = new JsonCommentProcessor(createGson());
            commentedWriter.writeWithComments(this.config, writer);
        } catch (IOException e) {
            Constants.LOG.error("Failed to write config file: {}", CONFIG_FILE.toString(), e);
        }
    }
}
