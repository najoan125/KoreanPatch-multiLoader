package com.hyfata.najoan.koreanpatch.mixin.mods.commandblockide;

import arm32x.minecraft.commandblockide.client.gui.MultilineTextFieldWidget;
import com.hyfata.najoan.koreanpatch.wrapper.WrapperEditBox;
import com.hyfata.najoan.koreanpatch.mixin.accessor.EditBoxAccessor;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MultilineTextFieldWidget.class)
public abstract class MultilineEditBoxMixin extends EditBox {
    public MultilineEditBoxMixin(Font textRenderer, int width, int height, Component text) {
        super(textRenderer, width, height, text);
    }

    @Unique
    private final WrapperEditBox handler = new WrapperEditBox((EditBoxAccessor) this);

    @Inject(at = @At(value = "HEAD"), method = "keyPressed", cancellable = true)
    public void keyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        handler.keyPressed(keyCode, cir);
    }
}
