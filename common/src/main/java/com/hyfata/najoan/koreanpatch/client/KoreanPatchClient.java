package com.hyfata.najoan.koreanpatch.client;

import com.hyfata.najoan.koreanpatch.data.ConfigManager;
import com.hyfata.najoan.koreanpatch.platform.Services;
import com.hyfata.najoan.koreanpatch.process.ime.InputController;
import com.hyfata.najoan.koreanpatch.process.ime.InputManager;

public class KoreanPatchClient {
    public static void init() {
        KeyBinds.register();
    }

    public static void clientStarted() {
        if (Services.PLATFORM.isModLoaded(Constants.MOD_ID)) {
            InputManager.applyController(InputController.newController());
            ConfigManager.getInstance().init();
            Constants.LOG.info("Korean Patch Loaded");
        }
    }
}