package com.hyfata.najoan.koreanpatch.mixin;

import com.hyfata.najoan.koreanpatch.client.KeyBinds;
import com.hyfata.najoan.koreanpatch.client.KoreanPatchClient;
import com.hyfata.najoan.koreanpatch.config.ConfigManager;
import com.hyfata.najoan.koreanpatch.config.category.CategoryInput;
import com.hyfata.najoan.koreanpatch.driver.arch.darwin.DarwinController;
import com.hyfata.najoan.koreanpatch.process.LanguageType;
import com.hyfata.najoan.koreanpatch.storage.InputStatusStorage;
import com.hyfata.najoan.koreanpatch.client.GUIStatus;
import com.hyfata.najoan.koreanpatch.driver.InputManager;
import com.hyfata.najoan.koreanpatch.process.LangTypeManager;
import com.sun.jna.Platform;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.input.KeyEvent;
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
        int modifiers = keyEvent.modifiers();
        CategoryInput categoryInput = ConfigManager.getInstance().getConfig().getCategoryInput();

        if (window == minecraft.getWindow().handle() && !GUIStatus.getInstance().isBypassInjection() && KoreanPatchClient.loaded) {
            // ime key
            if (KeyBinds.getImeBinding().matches(keyEvent) && action == 1 && modifiers == 2 &&
                    !categoryInput.isAlwaysImeEnabled()) {
                InputManager.getController().toggleFocus();
                if (categoryInput.isMemoryLangTypePerScreen())
                    InputStatusStorage.getInstance().add(minecraft.screen);
            }

            // lang key
            else if (KeyBinds.getLangBinding().matches(keyEvent) && action == 1 &&
                    (!Platform.isMac() || modifiers != 1 && modifiers != 2)) {
                LangTypeManager.getInstance().toggleCurrentType();
                if (categoryInput.isMemoryLangTypePerScreen())
                    InputStatusStorage.getInstance().add(minecraft.screen);
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
