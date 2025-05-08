package com.hyfata.najoan.koreanpatch.mixin.mods.easy_anvils;

import com.hyfata.najoan.koreanpatch.data.LangTypeManager;
import com.hyfata.najoan.koreanpatch.gui.GUIStatus;
import com.hyfata.najoan.koreanpatch.mixin.accessor.EditBoxAccessor;
import com.hyfata.najoan.koreanpatch.process.controller.mixin.EditBoxController;
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
    private final EditBoxController _$handler = new EditBoxController((EditBoxAccessor) this);

    @Inject(at = {@At(value = "HEAD")}, method = {"m_5534_"}, cancellable = true, remap = false)
    public void charTyped(char chr, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (this._$client.screen != null && !GUIStatus.isBypassInjection() &&
                LangTypeManager.getInstance().isKorean() && this.isEditable && Character.charCount(chr) == 1) {
            _$handler.typedTextField(chr, modifiers, cir);
        }
    }

    @Unique
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        Minecraft client = Minecraft.getInstance();
        if (client.screen != null && !GUIStatus.isBypassInjection()) {
            if (keyCode == GLFW.GLFW_KEY_BACKSPACE) {
                if (_$handler.onBackspaceKeyPressed()) {
                    return true;
                }
            }
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
