package com.hyfata.najoan.koreanpatch.mixin;

import com.hyfata.najoan.koreanpatch.mixin.accessor.EditBoxAccessor;
import com.hyfata.najoan.koreanpatch.wrapper.WrapperEditBox;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = {EditBox.class})
public abstract class EditBoxMixin {
    @Shadow
    protected abstract boolean isEditable();

    @Unique
    private final WrapperEditBox koreanPatch$wrapper = new WrapperEditBox((EditBoxAccessor) this);

    @Inject(at = {@At(value = "HEAD")}, method = {"charTyped"}, cancellable = true)
    public void charTyped(CharacterEvent chrEvent, CallbackInfoReturnable<Boolean> cir) {
            koreanPatch$wrapper.charTyped(chrEvent, cir, isEditable());
    }

    @Inject(at = {@At(value = "HEAD")}, method = {"keyPressed"}, cancellable = true)
    private void keyPressed(KeyEvent keyEvent, CallbackInfoReturnable<Boolean> callbackInfo) {
        koreanPatch$wrapper.keyPressed(keyEvent, callbackInfo);
    }
}

