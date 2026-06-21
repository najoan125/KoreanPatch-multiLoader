package com.hyfata.najoan.koreanpatch.mixin.mods.ftb.library;

import com.hyfata.najoan.koreanpatch.wrapper.ftb.library.WrapperFTBTextBox;
import dev.ftb.mods.ftblibrary.client.gui.input.Key;
import dev.ftb.mods.ftblibrary.client.gui.widget.TextBox;
import net.minecraft.client.input.CharacterEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = {TextBox.class})
public abstract class TextBoxMixin {
    @Unique
    private final WrapperFTBTextBox koreanPatch$wrapper = new WrapperFTBTextBox((TextBox) (Object) this);

    @Inject(at = {@At(value = "HEAD")}, method = {"charTyped"}, cancellable = true)
    public void charTyped(CharacterEvent event, CallbackInfoReturnable<Boolean> cir) {
        koreanPatch$wrapper.charTyped(event, cir, true);
    }

    @Inject(at = {@At(value = "HEAD")}, method = {"keyPressed"}, cancellable = true)
    private void keyPressed(Key key, CallbackInfoReturnable<Boolean> cir) {
        koreanPatch$wrapper.keyPressed(key, cir);
    }
}
