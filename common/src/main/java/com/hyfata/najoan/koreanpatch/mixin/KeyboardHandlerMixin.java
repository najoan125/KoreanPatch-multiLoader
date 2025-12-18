package com.hyfata.najoan.koreanpatch.mixin;

import com.hyfata.najoan.koreanpatch.client.KoreanPatchClient;
import com.hyfata.najoan.koreanpatch.config.ConfigManager;
import com.hyfata.najoan.koreanpatch.config.category.CategoryInput;
import com.hyfata.najoan.koreanpatch.driver.arch.darwin.DarwinController;
import com.hyfata.najoan.koreanpatch.keybinding.KeyBindingManager;
import com.hyfata.najoan.koreanpatch.process.LanguageType;
import com.hyfata.najoan.koreanpatch.storage.InputStatusStorage;
import com.hyfata.najoan.koreanpatch.client.GUIStatus;
import com.hyfata.najoan.koreanpatch.driver.InputManager;
import com.hyfata.najoan.koreanpatch.process.LangTypeManager;
import com.sun.jna.Platform;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.input.KeyEvent;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardHandler.class)
public class KeyboardHandlerMixin {
    @Shadow
    @Final
    private Minecraft minecraft;

    @Unique
    private final KeyBindingManager koreanPatch$keyBindingManager = KeyBindingManager.getInstance();

    @Unique
    private final ConfigManager koreanPatch$configManager = ConfigManager.getInstance();

    /**
     * action
     *  - 0: key up
     *  - 1: key down
     *  - 2: repeat
     */
    @Inject(method = "keyPress", at = @At("HEAD"))
    private void onInput(long window, int action, KeyEvent keyEvent, CallbackInfo ci) {
        int keyCode = keyEvent.key();
        int scanCode = keyEvent.scancode();

        // send key event to KeyBindingManager
        int glfwAction = action == 1 ? GLFW.GLFW_PRESS : (action == 0 ? GLFW.GLFW_RELEASE : GLFW.GLFW_REPEAT);
        koreanPatch$keyBindingManager.onKeyInput(keyCode, scanCode, glfwAction, 0);

        if (window == minecraft.getWindow().handle() && !GUIStatus.getInstance().isBypassInjection() && KoreanPatchClient.loaded) {
            // if the key is down
            if (action == 1) {
                koreanPatch$onKeyDown();
            }

            // fix mac capslock
            if (Platform.isMac() && action == 0 && keyCode == -1 && scanCode == 255) {
                DarwinController controller = (DarwinController) InputManager.getController();
                if (controller.isCapsLockOn()) {
                    LangTypeManager.getInstance().setCurrentType(LanguageType.EN);
                }
            }
        }
    }

    @Unique
    private void koreanPatch$onKeyDown() {
        CategoryInput categoryInput = koreanPatch$configManager.getConfig().getCategoryInput();
        // check IME toggle key
        if (!categoryInput.isAlwaysImeEnabled() && koreanPatch$keyBindingManager.isImeKeyPressed()) {
            InputManager.getController().toggleFocus();
            if (categoryInput.isMemoryLangTypePerScreen())
                InputStatusStorage.getInstance().add(minecraft.screen);
        }

        // check lang type toggle key
        if (koreanPatch$keyBindingManager.isLangTypeKeyPressed()) {
            LangTypeManager.getInstance().toggleCurrentType();
            if (categoryInput.isMemoryLangTypePerScreen())
                InputStatusStorage.getInstance().add(minecraft.screen);
        }
    }
}
