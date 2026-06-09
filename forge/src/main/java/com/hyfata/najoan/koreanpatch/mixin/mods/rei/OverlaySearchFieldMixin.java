package com.hyfata.najoan.koreanpatch.mixin.mods.rei;

import com.hyfata.najoan.koreanpatch.client.GUIStatus;
import com.hyfata.najoan.koreanpatch.process.LangTypeManager;
import com.hyfata.najoan.koreanpatch.wrapper.WrapperREITextField;
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
    private final WrapperREITextField koreanPatch$handler = new WrapperREITextField(this);

    @Inject(at = @At("HEAD"), method = "m_5534_", cancellable = true, remap = false)
    public void charTyped(char character, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (this.koreanPatch$client.screen != null && GUIStatus.getInstance().shouldApplyInjection() &&
                LangTypeManager.getInstance().isKorean() && Character.charCount(character) == 1) {
            koreanPatch$handler.typedTextField(character, modifiers, cir);
        }
    }

    @Inject(at = @At("HEAD"), method = "m_7933_", cancellable = true, remap = false)
    public void keyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        Minecraft client = Minecraft.getInstance();
        if (client.screen != null && GUIStatus.getInstance().shouldApplyInjection()) {
            if (keyCode == GLFW.GLFW_KEY_BACKSPACE) {
                if (koreanPatch$handler.onBackspaceKeyPressed()) {
                    cir.setReturnValue(Boolean.TRUE);
                }
            }
        }
    }
}
