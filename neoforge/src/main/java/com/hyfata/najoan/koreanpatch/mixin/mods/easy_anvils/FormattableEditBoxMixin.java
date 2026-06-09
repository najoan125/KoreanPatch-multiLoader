package com.hyfata.najoan.koreanpatch.mixin.mods.easy_anvils;

import com.hyfata.najoan.koreanpatch.wrapper.WrapperEditBox;
import com.hyfata.najoan.koreanpatch.mixin.accessor.EditBoxAccessor;
import fuzs.easyanvils.client.gui.components.AdvancedEditBox;
import fuzs.easyanvils.client.gui.components.FormattableEditBox;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = {FormattableEditBox.class})
public abstract class FormattableEditBoxMixin extends AdvancedEditBox {
    public FormattableEditBoxMixin(Font font, int x, int y, int width, int height, Component message) {
        super(font, x, y, width, height, message);
    }

    @Unique
    private final WrapperEditBox _$handler = new WrapperEditBox((EditBoxAccessor) this);

    @Inject(at = {@At(value = "HEAD")}, method = {"charTyped(CI)Z"}, cancellable = true)
    public void charTyped(char codePoint, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        _$handler.charTyped(codePoint, modifiers, cir, this.isEditable);
    }

    @Unique
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return _$handler.keyPressed(keyCode, () -> super.keyPressed(keyCode, scanCode, modifiers));
    }
}
