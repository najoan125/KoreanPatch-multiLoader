package com.hyfata.najoan.koreanpatch.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.sun.jna.Platform;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;

public class KeyBinds {
    private static final ArrayList<KeyMapping> keyMappings = new ArrayList<>();

    public static void register() {
        int keycode;
        if (Platform.isWindows()) {
            keycode = GLFW.GLFW_KEY_RIGHT_ALT;
        } else {
            keycode = GLFW.GLFW_KEY_LEFT_CONTROL;
        }

        // 0: lang binding
        keyMappings.add(new KeyMapping(
                "key.koreanpatch.toggle_langtype",
                InputConstants.Type.KEYSYM,
                keycode,
                "key.categories.koreanpatch"
        ));

        // 1: ime binding
        keyMappings.add(new KeyMapping(
                "key.koreanpatch.toggle_ime",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_I,
                "key.categories.koreanpatch"
        ));
    }

    public static ArrayList<KeyMapping> getKeyMappings() {
        return keyMappings;
    }

    public static KeyMapping getLangBinding() {
        return keyMappings.get(0);
    }

    public static KeyMapping getImeBinding() {
        return keyMappings.get(1);
    }
}
