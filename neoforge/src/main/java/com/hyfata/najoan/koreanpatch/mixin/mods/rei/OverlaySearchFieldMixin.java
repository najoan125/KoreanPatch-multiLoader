package com.hyfata.najoan.koreanpatch.mixin.mods.rei;

import com.hyfata.najoan.koreanpatch.client.KoreanPatchClient;
import com.hyfata.najoan.koreanpatch.handler.mixin.REITextFieldHandler;
import com.hyfata.najoan.koreanpatch.util.language.LanguageUtil;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.impl.client.gui.widget.basewidgets.TextFieldWidget;
import me.shedaniel.rei.impl.client.gui.widget.search.OverlaySearchField;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(OverlaySearchField.class)
public abstract class OverlaySearchFieldMixin extends TextFieldWidget {
    public OverlaySearchFieldMixin(Rectangle bounds) {
        super(bounds);
    }

    @Unique
    private final Minecraft koreanPatch$client = Minecraft.getInstance();

    @Unique
    private final REITextFieldHandler koreanPatch$handler = new REITextFieldHandler(this);

    @Inject(at = @At("HEAD"), method = "charTyped", cancellable = true)
    public void charTyped(char chr, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (this.koreanPatch$client.screen != null && !KoreanPatchClient.bypassInjection &&
                LanguageUtil.isKorean() && Character.charCount(chr) == 1) {
            koreanPatch$handler.typedTextField(chr, modifiers, cir);
        }
    }

    @Inject(at = @At("HEAD"), method = "keyPressed", cancellable = true)
    public void keyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        Minecraft client = Minecraft.getInstance();
        if (client.screen != null && !KoreanPatchClient.bypassInjection) {
            if (keyCode == GLFW.GLFW_KEY_BACKSPACE) {
                if (koreanPatch$handler.onBackspaceKeyPressed()) {
                    cir.setReturnValue(Boolean.TRUE);
                }
            }
        }
    }
}
