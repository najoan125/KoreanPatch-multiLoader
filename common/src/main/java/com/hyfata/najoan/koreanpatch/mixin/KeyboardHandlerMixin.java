package com.hyfata.najoan.koreanpatch.mixin;

import com.hyfata.najoan.koreanpatch.client.KeyBinds;
import com.hyfata.najoan.koreanpatch.data.ConfigManager;
import com.hyfata.najoan.koreanpatch.data.storage.InputStatusStorage;
import com.hyfata.najoan.koreanpatch.gui.GUIStatus;
import com.hyfata.najoan.koreanpatch.process.ime.InputManager;
import com.hyfata.najoan.koreanpatch.data.LangTypeManager;
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
        boolean memoryLangType = ConfigManager.getInstance().getConfig().getCategoryInput().isMemoryLangTypePerScreen();

        if (window == minecraft.getWindow().getWindow() && action == 1 && !GUIStatus.isBypassInjection()) {
            if (minecraft.screen != null && KeyBinds.getImeBinding().matches(keyCode, scanCode) && modifiers == 2) {
                InputManager.getController().toggleFocus();
                if (memoryLangType) InputStatusStorage.getInstance().add(minecraft.screen);
            } else if (KeyBinds.getLangBinding().matches(keyCode, scanCode) && !InputManager.getController().isFocused()) {
                LangTypeManager.getInstance().toggleCurrentType();
                if (memoryLangType) InputStatusStorage.getInstance().add(minecraft.screen);
            }
        }
    }
}
