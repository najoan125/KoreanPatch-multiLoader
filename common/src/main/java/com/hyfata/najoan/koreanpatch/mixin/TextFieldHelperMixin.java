package com.hyfata.najoan.koreanpatch.mixin;

import com.hyfata.najoan.koreanpatch.gui.GUIStatus;
import com.hyfata.najoan.koreanpatch.process.controller.mixin.TextFieldHelperController;
import com.hyfata.najoan.koreanpatch.mixin.accessor.TextFieldHelperAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.font.TextFieldHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = {TextFieldHelper.class})
public abstract class TextFieldHelperMixin {
    @Unique
    private final TextFieldHelperController koreanPatch$handler = new TextFieldHelperController((TextFieldHelperAccessor) this);

    @Inject(at = {@At(value = "HEAD")}, method = {"charTyped(C)Z"}, cancellable = true)
    public void insertChar(char chr, CallbackInfoReturnable<Boolean> cir) {
        koreanPatch$handler.insertChar(chr, cir);
    }

    @Inject(at = {@At(value = "HEAD")}, method = {"insertText(Ljava/lang/String;)V"}, cancellable = true)
    public void insertString(String string, CallbackInfo ci) {
        koreanPatch$handler.insertString(string, ci);
    }

    @Inject(at = {@At(value = "HEAD")}, method = {"removeCharsFromCursor(I)V"}, cancellable = true)
    public void delete(int offset, CallbackInfo ci) {
        Minecraft client = Minecraft.getInstance();
        if (client.screen != null && !GUIStatus.isBypassInjection()) {
            if (koreanPatch$handler.onBackspaceKeyPressed()) {
                ci.cancel();
            }
        }
    }
}

