package com.hyfata.najoan.koreanpatch.mixin;

import com.hyfata.najoan.koreanpatch.client.KoreanPatchClient;
import com.hyfata.najoan.koreanpatch.util.language.LanguageUtil;
import com.hyfata.najoan.koreanpatch.handler.mixin.editbox.IEditBoxAccessor;
import com.hyfata.najoan.koreanpatch.handler.mixin.editbox.EditBoxHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Consumer;

@Mixin(value = {EditBox.class})
public abstract class EditBoxMixin implements IEditBoxAccessor {
    @Shadow
    private Consumer<String> responder;
    @Unique
    private final Minecraft koreanPatch$client = Minecraft.getInstance();

    @Shadow
    public abstract int getCursorPosition();

    @Shadow
    public abstract void moveCursorTo(int var1, boolean shift);

    @Shadow
    public abstract void deleteChars(int var1);

    @Shadow
    public abstract String getValue();

    @Shadow
    public abstract void insertText(String var1);

    @Shadow
    protected abstract boolean isEditable();

    @Shadow
    protected abstract void onValueChange(String var1);

    @Shadow
    public abstract void setValue(String var1);

    @Shadow
    public abstract boolean canConsumeInput();

    @Shadow
    public abstract String getHighlighted();

    @Override
    public Consumer<String> koreanPatch$getChangedListener() {
        return this.responder;
    }

    @Override
    public void koreanPatch$changed(String var1) {
        onValueChange(var1);
    }

    @Unique
    private final EditBoxHandler koreanPatch$handler = new EditBoxHandler(this);

    @Inject(at = {@At(value = "HEAD")}, method = {"charTyped(CI)Z"}, cancellable = true)
    public void charTyped(char chr, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (this.koreanPatch$client.screen != null && !KoreanPatchClient.bypassInjection &&
                LanguageUtil.isKorean() && this.isEditable() && Character.charCount(chr) == 1) {
            koreanPatch$handler.typedTextField(chr, modifiers, cir);
        }
    }

    @Inject(at = {@At(value = "HEAD")}, method = {"keyPressed(III)Z"}, cancellable = true)
    private void keyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> callbackInfo) {
        Minecraft client = Minecraft.getInstance();
        if (client.screen != null && !KoreanPatchClient.bypassInjection) {
            if (keyCode == GLFW.GLFW_KEY_BACKSPACE) {
                if (koreanPatch$handler.onBackspaceKeyPressed()) {
                    callbackInfo.setReturnValue(Boolean.TRUE);
                }
            }
        }
    }
}

