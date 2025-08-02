package com.hyfata.najoan.koreanpatch.mixin.mods.easy_anvils;

import com.hyfata.najoan.koreanpatch.client.GUIStatus;
import com.hyfata.najoan.koreanpatch.wrapper.WrapperEditBox;
import com.hyfata.najoan.koreanpatch.mixin.accessor.EditBoxAccessor;
import com.hyfata.najoan.koreanpatch.process.LangTypeManager;
import fuzs.easyanvils.client.gui.components.AdvancedEditBox;
import fuzs.easyanvils.client.gui.components.FormattableEditBox;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;
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
    private final Minecraft koreanPatch$client = Minecraft.getInstance();

    @Unique
    private final WrapperEditBox koreanPatch$handler = new WrapperEditBox((EditBoxAccessor) this);

    @Inject(at = {@At(value = "HEAD")}, method = {"charTyped(CI)Z"}, cancellable = true)
    public void charTyped(char chr, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (this.koreanPatch$client.screen != null && !GUIStatus.getInstance().isBypassInjection() &&
                LangTypeManager.getInstance().isKorean() && this.isEditable() && Character.charCount(chr) == 1) {
            koreanPatch$handler.charTyped(chr, modifiers, cir);
        }
    }

    @Unique
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        Minecraft client = Minecraft.getInstance();
        if (client.screen != null && !GUIStatus.getInstance().isBypassInjection()) {
            if (keyCode == GLFW.GLFW_KEY_BACKSPACE) {
                if (koreanPatch$handler.onBackspaceKeyPressed()) {
                    return true;
                }
            }
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
