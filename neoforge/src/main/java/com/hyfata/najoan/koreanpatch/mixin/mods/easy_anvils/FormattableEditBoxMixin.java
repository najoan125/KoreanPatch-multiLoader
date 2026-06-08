package com.hyfata.najoan.koreanpatch.mixin.mods.easy_anvils;

import com.hyfata.najoan.koreanpatch.wrapper.WrapperEditBox;
import com.hyfata.najoan.koreanpatch.mixin.accessor.EditBoxAccessor;
import fuzs.easyanvils.client.gui.components.FormattableEditBox;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = {FormattableEditBox.class})
public abstract class FormattableEditBoxMixin extends EditBox {
    public FormattableEditBoxMixin(Font font, int x, int y, int width, int height, Component message) {
        super(font, x, y, width, height, message);
    }

    @Unique
    private final WrapperEditBox koreanPatch$wrapper = new WrapperEditBox((EditBoxAccessor) this);

    @Inject(at = {@At(value = "HEAD")}, method = {"charTyped"}, cancellable = true)
    public void charTyped(CharacterEvent characterEvent, CallbackInfoReturnable<Boolean> cir) {
        koreanPatch$wrapper.charTyped(characterEvent, cir, isEditable);
    }

    @Unique
    @Override
    public boolean keyPressed(@NotNull KeyEvent keyEvent) {
        return koreanPatch$wrapper.keyPressed(keyEvent, () -> super.keyPressed(keyEvent));
    }
}
