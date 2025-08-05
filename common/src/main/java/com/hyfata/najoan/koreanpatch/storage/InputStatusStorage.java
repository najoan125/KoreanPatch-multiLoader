package com.hyfata.najoan.koreanpatch.storage;

import com.hyfata.najoan.koreanpatch.client.Constants;
import com.hyfata.najoan.koreanpatch.process.LangTypeManager;
import com.hyfata.najoan.koreanpatch.process.LanguageType;
import com.hyfata.najoan.koreanpatch.driver.InputManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class InputStatusStorage {
    private static InputStatusStorage instance;

    public static InputStatusStorage getInstance() {
        if (instance == null) {
            instance = new InputStatusStorage();
        }
        return instance;
    }

    private final File STATUS_FILE = new File(Minecraft.getInstance().gameDirectory,
            Constants.MOD_ID + ".bin");
    private final Map<String, InputStatus> statusMap = new HashMap<>();

    public void add(Screen screen) {
        add(screen, LangTypeManager.getInstance().getCurrentType(), InputManager.getController().isFocused());
    }

    public void add(Screen screen, LanguageType languageType, boolean imeFocus) {
        if (screen == null) return;

        String screenName = screen.getClass().getName();
        if (statusMap.containsKey(screenName)) {
            InputStatus status = statusMap.get(screenName);
            status.setLanguageType(languageType);
            status.setImeFocus(imeFocus);
        } else {
            InputStatus status = new InputStatus(languageType, imeFocus);
            statusMap.put(screenName, status);
        }
    }

    public InputStatus get(Screen screen) {
        if (screen == null) return null;
        return statusMap.get(screen.getClass().getName());
    }

    public void save() {
        try (FileOutputStream fos = new FileOutputStream(STATUS_FILE);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(statusMap);
            oos.flush();
        } catch (IOException e) {
            Constants.LOG.error("Failed to save input status file: {}", STATUS_FILE, e);
        }
    }

    @SuppressWarnings("unchecked")
    public void load() {
        if (!STATUS_FILE.exists()) return;

        try (FileInputStream fis = new FileInputStream(STATUS_FILE);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            statusMap.clear();
            statusMap.putAll((Map<String, InputStatus>) ois.readObject());
        } catch (IOException | ClassNotFoundException e) {
            Constants.LOG.error("Failed to load input status file: {}", STATUS_FILE, e);
        }
    }
}
