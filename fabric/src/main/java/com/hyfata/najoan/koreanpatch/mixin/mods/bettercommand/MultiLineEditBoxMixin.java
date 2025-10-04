package com.hyfata.najoan.koreanpatch.mixin.mods.bettercommand;

import bettercommandblockui.main.ui.MultiLineTextFieldWidget;
import com.hyfata.najoan.koreanpatch.mixin.accessor.EditBoxAccessor;
import com.hyfata.najoan.koreanpatch.wrapper.WrapperEditBox;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MultiLineTextFieldWidget.class)
public abstract class MultiLineEditBoxMixin extends EditBox {
    public MultiLineEditBoxMixin(Font textRenderer, int width, int height, Component text) {
        super(textRenderer, width, height, text);
    }

    @Unique
    private final WrapperEditBox handler = new WrapperEditBox((EditBoxAccessor) this);

    @Inject(method = "charTyped", at = @At("HEAD"), cancellable = true)
    private void charTyped(char chr, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        handler.charTyped(chr, modifiers, cir, this.isEditable());
    }

    @Inject(at = @At(value = "HEAD"), method = "keyPressed", cancellable = true)
    public void keyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        handler.keyPressed(keyCode, cir);
    }
}
