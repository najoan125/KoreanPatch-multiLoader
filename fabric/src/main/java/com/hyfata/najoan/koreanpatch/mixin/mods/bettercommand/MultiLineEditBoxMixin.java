package com.hyfata.najoan.koreanpatch.mixin.mods.bettercommand;

import bettercommandblockui.main.ui.MultiLineTextFieldWidget;
import com.hyfata.najoan.koreanpatch.mixin.accessor.EditBoxAccessor;
import com.hyfata.najoan.koreanpatch.wrapper.WrapperEditBox;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
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
    private final WrapperEditBox wrapper = new WrapperEditBox((EditBoxAccessor) this);

    @Inject(method = "charTyped", at = @At("HEAD"), cancellable = true)
    private void charTyped(CharacterEvent input, CallbackInfoReturnable<Boolean> cir) {
        wrapper.charTyped(input, cir, this.isEditable());
    }

    @Inject(at = @At(value = "HEAD"), method = "keyPressed", cancellable = true)
    public void keyPressed(KeyEvent input, CallbackInfoReturnable<Boolean> cir) {
        wrapper.keyPressed(input, cir);
    }
}
