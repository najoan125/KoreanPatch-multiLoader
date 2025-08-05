package com.hyfata.najoan.koreanpatch.mixin;

import com.hyfata.najoan.koreanpatch.client.GUIStatus;
import com.hyfata.najoan.koreanpatch.mixin.accessor.EditBoxAccessor;
import com.hyfata.najoan.koreanpatch.process.LangTypeManager;
import com.hyfata.najoan.koreanpatch.wrapper.WrapperEditBox;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = {EditBox.class})
public abstract class EditBoxMixin {
    @Shadow
    public abstract boolean isEditable();

    @Unique
    private final Minecraft koreanPatch$mc = Minecraft.getInstance();

    @Unique
    private final WrapperEditBox koreanPatch$wrapper = new WrapperEditBox((EditBoxAccessor) this);

    @Inject(at = {@At(value = "HEAD")}, method = {"charTyped(CI)Z"}, cancellable = true)
    public void charTyped(char chr, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (this.koreanPatch$mc.screen != null && !GUIStatus.getInstance().isBypassInjection() &&
                LangTypeManager.getInstance().isKorean() && this.isEditable() && Character.charCount(chr) == 1) {
            koreanPatch$wrapper.charTyped(chr, modifiers, cir);
        }
    }

    @Inject(at = {@At(value = "HEAD")}, method = {"keyPressed(III)Z"}, cancellable = true)
    private void keyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> callbackInfo) {
        Minecraft client = Minecraft.getInstance();
        if (client.screen != null && !GUIStatus.getInstance().isBypassInjection()) {
            if (keyCode == GLFW.GLFW_KEY_BACKSPACE) {
                if (koreanPatch$wrapper.onBackspaceKeyPressed()) {
                    callbackInfo.setReturnValue(Boolean.TRUE);
                }
            }
        }
    }
}

