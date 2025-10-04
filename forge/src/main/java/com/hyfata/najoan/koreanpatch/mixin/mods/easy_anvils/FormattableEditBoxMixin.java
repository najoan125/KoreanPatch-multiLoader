package com.hyfata.najoan.koreanpatch.mixin.mods.easy_anvils;

import com.hyfata.najoan.koreanpatch.mixin.accessor.EditBoxAccessor;
import com.hyfata.najoan.koreanpatch.wrapper.WrapperEditBox;
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
    private final WrapperEditBox koreanPatch$wrapper = new WrapperEditBox((EditBoxAccessor) this);

    @Inject(at = {@At(value = "HEAD")}, method = {"m_5534_"}, cancellable = true, remap = false)
    public void charTyped(char chr, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        koreanPatch$wrapper.charTyped(chr, modifiers, cir, this.isEditable);
    }

    @Unique
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return koreanPatch$wrapper.keyPressed(keyCode, () -> super.keyPressed(keyCode, scanCode, modifiers));
    }
}
