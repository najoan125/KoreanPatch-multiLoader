package com.hyfata.najoan.koreanpatch.client;

import com.hyfata.najoan.koreanpatch.config.ConfigManager;
import com.hyfata.najoan.koreanpatch.keybinding.KeyBindingManager;
import com.hyfata.najoan.koreanpatch.storage.InputStatusStorage;
import com.hyfata.najoan.koreanpatch.platform.Services;
import com.hyfata.najoan.koreanpatch.driver.InputController;
import com.hyfata.najoan.koreanpatch.driver.InputManager;

public class KoreanPatchClient {
    public static boolean loaded = false;

    public static void init() {
        KeyBinds.register();
    }

    public static void clientStarted() {
        if (Services.PLATFORM.isModLoaded(Constants.MOD_ID)) {
            InputManager.applyController(InputController.newController());
            ConfigManager.getInstance().init();
            InputStatusStorage.getInstance().load();
            KeyBindingManager.getInstance(); // 키바인딩 매니저 초기화
            loaded = true;
            Constants.LOG.info("Korean Patch Loaded");
        }
    }
}