package com.hyfata.najoan.koreanpatch.client;

import com.hyfata.najoan.koreanpatch.platform.Services;
import com.hyfata.najoan.koreanpatch.plugin.InputController;
import com.hyfata.najoan.koreanpatch.plugin.InputManager;

public class KoreanPatchClient {
    public static boolean IME = false;
    public static boolean bypassInjection = false;

    public static void init() {
        if (Services.PLATFORM.isModLoaded("koreanpatch")) {
            InputManager.applyController(InputController.newController());
            Constants.LOG.info("Korean Patch Loaded");
        }
    }
}