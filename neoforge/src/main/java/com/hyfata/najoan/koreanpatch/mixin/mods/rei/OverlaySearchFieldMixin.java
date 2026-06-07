package com.hyfata.najoan.koreanpatch.mixin.mods.rei;

import com.hyfata.najoan.koreanpatch.client.GUIStatus;
import com.hyfata.najoan.koreanpatch.wrapper.WrapperREITextField;
import com.hyfata.najoan.koreanpatch.process.LangTypeManager;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.impl.client.gui.widget.basewidgets.TextFieldWidget;
import me.shedaniel.rei.impl.client.gui.widget.search.OverlaySearchField;
import net.minecraft.client.Minecraft;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
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
    private final WrapperREITextField koreanPatch$handler = new WrapperREITextField(this);

    @Inject(at = @At("HEAD"), method = "charTyped", cancellable = true)
    public void charTyped(CharacterEvent event, CallbackInfoReturnable<Boolean> cir) {
        char chr = (char) event.codepoint();
        if (this.koreanPatch$client.screen != null && !GUIStatus.getInstance().isBypassInjection() &&
                LangTypeManager.getInstance().isKorean() && Character.charCount(chr) == 1) {
            koreanPatch$handler.typedTextField(event, cir);
        }
    }

    @Inject(at = @At("HEAD"), method = "keyPressed", cancellable = true)
    public void keyPressed(KeyEvent event, CallbackInfoReturnable<Boolean> cir) {
        Minecraft client = Minecraft.getInstance();
        if (client.screen != null && !GUIStatus.getInstance().isBypassInjection()) {
            if (event.key() == GLFW.GLFW_KEY_BACKSPACE) {
                if (koreanPatch$handler.onBackspaceKeyPressed()) {
                    cir.setReturnValue(Boolean.TRUE);
                }
            }
        }
    }
}
