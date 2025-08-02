package com.hyfata.najoan.koreanpatch.mixin;

import com.hyfata.najoan.koreanpatch.client.KeyBinds;
import com.hyfata.najoan.koreanpatch.config.ConfigManager;
import com.hyfata.najoan.koreanpatch.config.category.CategoryInput;
import com.hyfata.najoan.koreanpatch.storage.InputStatusStorage;
import com.hyfata.najoan.koreanpatch.client.GUIStatus;
import com.hyfata.najoan.koreanpatch.driver.InputManager;
import com.hyfata.najoan.koreanpatch.process.LangTypeManager;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
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

    @Inject(method = "keyPress", at = @At("HEAD"))
    private void onInput(long window, int keyCode, int scanCode, int action, int modifiers, CallbackInfo ci) {
        CategoryInput categoryInput = ConfigManager.getInstance().getConfig().getCategoryInput();

        if (window == minecraft.getWindow().getWindow() && action == 1 && !GUIStatus.isBypassInjection()) {
            if (KeyBinds.getImeBinding().matches(keyCode, scanCode) && modifiers == 2 && !categoryInput.isAlwaysImeEnabled()) {
                InputManager.getController().toggleFocus();
                if (categoryInput.isMemoryLangTypePerScreen())
                    InputStatusStorage.getInstance().add(minecraft.screen);

            } else if (KeyBinds.getLangBinding().matches(keyCode, scanCode)) {
                LangTypeManager.getInstance().toggleCurrentType();
                if (categoryInput.isMemoryLangTypePerScreen())
                    InputStatusStorage.getInstance().add(minecraft.screen);
            }
        }
    }
}
