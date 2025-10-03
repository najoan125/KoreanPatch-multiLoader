package com.hyfata.najoan.koreanpatch.mixin;

import com.hyfata.najoan.koreanpatch.mixin.accessor.MultilineTextFieldAccessor;
import com.hyfata.najoan.koreanpatch.wrapper.WrapperMultilineTextField;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractTextAreaWidget;
import net.minecraft.client.gui.components.MultiLineEditBox;
import net.minecraft.client.gui.components.MultilineTextField;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MultiLineEditBox.class)
public abstract class MultilineEditBoxMixin extends AbstractTextAreaWidget {
    @Final
    @Shadow
    private MultilineTextField textField;

    @Unique
    private WrapperMultilineTextField koreanPatch$wrapper;

    public MultilineEditBoxMixin(int x, int y, int width, int height, Component message) {
        super(x, y, width, height, message);
    }

    @Inject(at = {@At(value = "TAIL")}, method = {"<init>"})
    public void init(Font font, int x, int y, int width, int height, Component placeholder, Component message, int textColor, boolean textShadow, int cursorColor, boolean showBackground, boolean showDecorations, CallbackInfo ci) {
        koreanPatch$wrapper = new WrapperMultilineTextField((MultilineTextFieldAccessor) this.textField);
    }

    @Inject(at = {@At(value = "HEAD")}, method = {"charTyped(CI)Z"}, cancellable = true)
    public void charTyped(char chr, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        koreanPatch$wrapper.charTyped(chr, modifiers, cir, this.visible, this.isFocused());
    }

    @Inject(at = {@At(value = "HEAD")}, method = {"keyPressed(III)Z"}, cancellable = true)
    private void keyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> callbackInfo) {
        koreanPatch$wrapper.keyPressed(keyCode, callbackInfo);
    }
}
