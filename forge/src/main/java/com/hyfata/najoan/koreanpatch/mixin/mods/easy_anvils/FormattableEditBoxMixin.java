package com.hyfata.najoan.koreanpatch.mixin.mods.easy_anvils;

import com.hyfata.najoan.koreanpatch.client.GUIStatus;
import com.hyfata.najoan.koreanpatch.mixin.accessor.EditBoxAccessor;
import com.hyfata.najoan.koreanpatch.process.LangTypeManager;
import com.hyfata.najoan.koreanpatch.wrapper.WrapperEditBox;
import fuzs.easyanvils.client.gui.components.OpenEditBox;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = {OpenEditBox.class})
public abstract class FormattableEditBoxMixin extends EditBox {
    public FormattableEditBoxMixin(Font font, int x, int y, int width, int height, Component message) {
        super(font, x, y, width, height, message);
    }

    @Unique
    private final Minecraft _$client = Minecraft.getInstance();

    @Unique
    private final WrapperEditBox _$handler = new WrapperEditBox((EditBoxAccessor) this);

    @Inject(at = {@At(value = "HEAD")}, method = {"m_5534_"}, cancellable = true, remap = false)
    public void charTyped(char chr, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (this._$client.screen != null && !GUIStatus.getInstance().isBypassInjection() &&
                LangTypeManager.getInstance().isKorean() && this.isEditable && Character.charCount(chr) == 1) {
            _$handler.charTyped(chr, modifiers, cir);
        }
    }

    @Unique
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        Minecraft client = Minecraft.getInstance();
        if (client.screen != null && !GUIStatus.getInstance().isBypassInjection()) {
            if (keyCode == GLFW.GLFW_KEY_BACKSPACE) {
                if (_$handler.onBackspaceKeyPressed()) {
                    return true;
                }
            }
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
