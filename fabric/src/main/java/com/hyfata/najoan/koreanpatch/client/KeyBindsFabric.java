package com.hyfata.najoan.koreanpatch.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.sun.jna.Platform;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class KeyBindsFabric {
    private static KeyMapping langBinding, imeBinding;

    public static void register() {
        int keycode;
        if (Platform.isWindows()) {
            keycode = GLFW.GLFW_KEY_RIGHT_ALT;
        } else {
            keycode = GLFW.GLFW_KEY_LEFT_CONTROL;
        }

        langBinding = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.koreanpatch.toggle_langtype",
                InputConstants.Type.KEYSYM,
                keycode,
                "key.categories.koreanpatch"
        ));

        imeBinding = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.koreanpatch.toggle_ime",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_I,
                "key.categories.koreanpatch"
        ));
    }

    public static KeyMapping getLangBinding() {
        return langBinding;
    }

    public static KeyMapping getImeBinding() {
        return imeBinding;
    }
}
