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
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardHandler.class)
public class KeyboardHandlerMixin {
    @Shadow
    @Final
    private Minecraft minecraft;

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
        CategoryInput categoryInput = ConfigManager.getInstance().getConfig().getCategoryInput();

        // KeyBindingManager에 키 이벤트 전달
        int glfwAction = action == 1 ? GLFW.GLFW_PRESS : (action == 0 ? GLFW.GLFW_RELEASE : GLFW.GLFW_REPEAT);
        KeyBindingManager.getInstance().onKeyInput(keyCode, scanCode, glfwAction, 0);

        if (window == minecraft.getWindow().handle() && !GUIStatus.getInstance().isBypassInjection() && KoreanPatchClient.loaded) {
            // if the key is down
            if (action == 1) {
                // check IME toggle key
                if (!categoryInput.isAlwaysImeEnabled()) {
                    if (KeyBindingManager.getInstance().isImeKeyPressed()) {
                        InputManager.getController().toggleFocus();
                        if (categoryInput.isMemoryLangTypePerScreen())
                            InputStatusStorage.getInstance().add(minecraft.screen);
                    }
                }

                // 한/영 변환키 체크
                if (KeyBindingManager.getInstance().isLangTypeKeyPressed()) {
                    LangTypeManager.getInstance().toggleCurrentType();
                    if (categoryInput.isMemoryLangTypePerScreen())
                        InputStatusStorage.getInstance().add(minecraft.screen);
                }
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
}
