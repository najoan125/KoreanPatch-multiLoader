package com.hyfata.najoan.koreanpatch.mixin.mods.easy_anvils;

import com.hyfata.najoan.koreanpatch.client.KoreanPatchClient;
import com.hyfata.najoan.koreanpatch.handler.mixin.EditBoxHandler;
import com.hyfata.najoan.koreanpatch.mixin.accessor.EditBoxAccessor;
import com.hyfata.najoan.koreanpatch.util.language.LanguageUtil;
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
    private final Minecraft _$client = Minecraft.getInstance();

    @Unique
    private final EditBoxHandler _$handler = new EditBoxHandler((EditBoxAccessor) this);

    @Inject(at = {@At(value = "HEAD")}, method = {"m_5534_"}, cancellable = true, remap = false)
    public void charTyped(char chr, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (this._$client.screen != null && !KoreanPatchClient.bypassInjection &&
                LanguageUtil.isKorean() && this.isEditable && Character.charCount(chr) == 1) {
            _$handler.typedTextField(chr, modifiers, cir);
        }
    }

    @Unique
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        Minecraft client = Minecraft.getInstance();
        if (client.screen != null && !KoreanPatchClient.bypassInjection) {
            if (keyCode == GLFW.GLFW_KEY_BACKSPACE) {
                if (_$handler.onBackspaceKeyPressed()) {
                    return true;
                }
            }
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
