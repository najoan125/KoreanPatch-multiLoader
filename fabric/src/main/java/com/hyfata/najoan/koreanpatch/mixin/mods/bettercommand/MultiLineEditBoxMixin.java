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
    public MultiLineEditBoxMixin(Font font, int x, int y, int width, int height, Component message) {
        super(font, x, y, width, height, message);
    }

    @Unique
    private final WrapperEditBox handler = new WrapperEditBox((EditBoxAccessor) this);

    @Inject(method = "charTyped", at = @At("HEAD"), cancellable = true)
    private void charTyped(char codePoint, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        handler.charTyped(codePoint, modifiers, cir, this.isEditable());
    }

    @Inject(at = @At(value = "HEAD"), method = "keyPressed", cancellable = true)
    public void keyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        handler.keyPressed(keyCode, cir);
    }
}
