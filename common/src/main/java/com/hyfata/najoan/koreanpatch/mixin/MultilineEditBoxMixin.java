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

    public MultilineEditBoxMixin(int p_388859_, int p_387520_, int p_387683_, int p_387659_, Component p_386737_) {
        super(p_388859_, p_387520_, p_387683_, p_387659_, p_386737_);
    }

    @Inject(at = {@At(value = "TAIL")}, method = {"<init>"})
    public void init(Font font, int x, int y, int width, int height, Component placeholder, Component message, int p_421931_, boolean p_421976_, int p_422714_, boolean p_422534_, boolean p_422133_, CallbackInfo ci) {
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
