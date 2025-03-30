package com.hyfata.najoan.koreanpatch.client;

import com.hyfata.najoan.koreanpatch.config.ModConfig;
import com.hyfata.najoan.koreanpatch.platform.Services;
import com.hyfata.najoan.koreanpatch.ime.controller.InputController;
import com.hyfata.najoan.koreanpatch.ime.controller.InputManager;
import me.shedaniel.autoconfig.AutoConfig;

public class KoreanPatchClient {
    public static boolean IME = false;
    public static boolean axiomEditorUIOpened = false;
    public static boolean bypassInjection = false;
    public static ModConfig config;

    public static void init() {
        config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
        KeyBinds.register();
    }

    public static void clientStarted() {
        if (Services.PLATFORM.isModLoaded(Constants.MOD_ID)) {
            InputManager.applyController(InputController.newController());
            Constants.LOG.info("Korean Patch Loaded");
        }
    }
}