package com.hyfata.najoan.koreanpatch.mixin.mods.bettercommand;

import bettercommandblockui.main.ui.MultiLineTextFieldWidget;
import com.hyfata.najoan.koreanpatch.client.KoreanPatchClient;
import com.hyfata.najoan.koreanpatch.mixin.accessor.EditBoxAccessor;
import com.hyfata.najoan.koreanpatch.util.language.LanguageUtil;
import com.hyfata.najoan.koreanpatch.handler.mixin.EditBoxHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MultiLineTextFieldWidget.class)
public abstract class MultiLineEditBoxMixin extends EditBox {
    public MultiLineEditBoxMixin(Font font, int x, int y, int width, int height, Component message) {
        super(font, x, y, width, height, message);
    }

    @Unique
    private final EditBoxHandler handler = new EditBoxHandler((EditBoxAccessor) this);

    @Inject(method = "charTyped", at = @At("HEAD"), cancellable = true)
    private void charTyped(char chr, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        Minecraft client = Minecraft.getInstance();
        if (client.screen != null && !KoreanPatchClient.bypassInjection &&
                LanguageUtil.isKorean() && this.isEditable() && Character.charCount(chr) == 1) {
            handler.typedTextField(chr, modifiers, cir);
        }
    }

    @Inject(at = @At(value = "HEAD"), method = "keyPressed", cancellable = true)
    public void keyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        Minecraft client = Minecraft.getInstance();
        if (client.screen != null && !KoreanPatchClient.bypassInjection) {
            if (keyCode == GLFW.GLFW_KEY_BACKSPACE) {
                if (handler.onBackspaceKeyPressed()) {
                    cir.setReturnValue(Boolean.TRUE);
                }
            }
        }
    }
}
