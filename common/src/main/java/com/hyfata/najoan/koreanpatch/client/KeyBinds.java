package com.hyfata.najoan.koreanpatch.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.sun.jna.Platform;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;

public class KeyBinds {
    private static final ArrayList<KeyMapping> keyMappings = new ArrayList<>();

    public static void register() {
        int keycode = GLFW.GLFW_KEY_LEFT_CONTROL;
        if (Platform.isWindows()) {
            keycode = GLFW.GLFW_KEY_RIGHT_ALT;
        } else if (Platform.isMac()) {
            keycode = GLFW.GLFW_KEY_CAPS_LOCK;
        }

        KeyMapping.Category koreanPatchCategory = new KeyMapping.Category(
                new ResourceLocation(Constants.MOD_ID, "keybinds")
        );

        // 0: lang binding
        keyMappings.add(new KeyMapping(
                "key.koreanpatch.toggle_langtype",
                InputConstants.Type.KEYSYM,
                keycode,
                koreanPatchCategory
        ));

        // 1: ime binding
        keyMappings.add(new KeyMapping(
                "key.koreanpatch.toggle_ime",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_I,
                koreanPatchCategory
        ));
    }

    public static ArrayList<KeyMapping> getKeyMappings() {
        return keyMappings;
    }

    public static KeyMapping getLangBinding() {
        return keyMappings.getFirst();
    }

    public static KeyMapping getImeBinding() {
        return keyMappings.get(1);
    }
}
