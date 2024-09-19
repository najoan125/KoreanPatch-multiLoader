package com.hyfata.najoan.koreanpatch.mixin;

import com.hyfata.najoan.koreanpatch.client.KeyBinds;
import com.hyfata.najoan.koreanpatch.client.KoreanPatchClient;
import com.hyfata.najoan.koreanpatch.plugin.InputManager;
import com.hyfata.najoan.koreanpatch.util.language.LanguageUtil;
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
        if (window == minecraft.getWindow().getWindow() && action == 1 && minecraft.screen != null && !KoreanPatchClient.bypassInjection) {
            if (KeyBinds.getImeBinding().matches(keyCode, scanCode) && modifiers == 2) {
                InputManager.getController().toggleFocus();
            } else if (KeyBinds.getLangBinding().matches(keyCode, scanCode) && !KoreanPatchClient.IME) {
                LanguageUtil.toggleCurrentType();
            }
        }
    }
}
