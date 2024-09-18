package com.hyfata.najoan.koreanpatch.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.sun.jna.Platform;
import net.minecraft.client.KeyMapping;
import net.neoforged.jarjar.nio.util.Lazy;
import org.lwjgl.glfw.GLFW;

public class KeyBindsNeoForge {
    private static Lazy<KeyMapping> langBinding, imeBinding;

    public static void register() {
        int keycode;
        if (Platform.isWindows()) {
            keycode = GLFW.GLFW_KEY_RIGHT_ALT;
        } else {
            keycode = GLFW.GLFW_KEY_LEFT_CONTROL;
        }

        langBinding = Lazy.of(() -> new KeyMapping(
                "key.koreanpatch.toggle_langtype",
                InputConstants.Type.KEYSYM,
                keycode,
                "key.categories.koreanpatch"
        ));

        imeBinding = Lazy.of(() -> new KeyMapping(
                "key.koreanpatch.toggle_ime",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_I,
                "key.categories.koreanpatch"
        ));
    }

    public static KeyMapping getLangBinding() {
        return langBinding.get();
    }

    public static KeyMapping getImeBinding() {
        return imeBinding.get();
    }
}
