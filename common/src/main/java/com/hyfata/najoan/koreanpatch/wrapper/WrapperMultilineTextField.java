package com.hyfata.najoan.koreanpatch.wrapper;

import com.hyfata.najoan.koreanpatch.client.GUIStatus;
import com.hyfata.najoan.koreanpatch.process.LangTypeManager;
import com.hyfata.najoan.koreanpatch.process.keyboard.KeyboardLayout;
import com.hyfata.najoan.koreanpatch.mixin.accessor.MultilineTextFieldAccessor;
import com.hyfata.najoan.koreanpatch.wrapper.handler.IMEWrapperHandler;
import com.hyfata.najoan.koreanpatch.process.HangulProcessor;
import com.hyfata.najoan.koreanpatch.util.HangulUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.StringUtil;
import net.minecraft.util.Mth;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.Callable;

public class WrapperMultilineTextField implements InterfaceIMEWrapper {
    private final MultilineTextFieldAccessor accessor;

    public WrapperMultilineTextField(MultilineTextFieldAccessor accessor) {
        this.accessor = accessor;
    }

    @Override
    public int getCursor() {
        return accessor.getCursor();
    }

    @Override
    public void writeText(String str) {
        accessor.invokeInsertText(str);
    }

    @Override
    public void modifyText(String str) {
        // deleteText()
        if (!accessor.invokeHasSelection()) {
            accessor.setSelectCursor(Mth.clamp(accessor.getCursor() - 1, 0, accessor.getValue().length()));
        }

        accessor.invokeInsertText(str);
    }

    private boolean onBackspaceKeyPressed() {
        if (accessor.invokeHasSelection()) {
            return false;
        }

        int cursorPosition = getCursor();
        return IMEWrapperHandler.onBackspaceKeyPressed(this, cursorPosition, accessor.getValue());
    }

    private boolean onHangulCharTyped(int keyCode, int modifiers) {
        return IMEWrapperHandler.onHangulCharTyped(this, keyCode, modifiers, accessor.getValue(), !accessor.invokeHasSelection());
    }

    private boolean validateKeyPressed(int keyCode) {
        Minecraft client = Minecraft.getInstance();
        if (client.screen != null &&
                GUIStatus.getInstance().shouldApplyInjection() &&
                keyCode == GLFW.GLFW_KEY_BACKSPACE) {
            return onBackspaceKeyPressed();
        }
        return false;
    }

    public void keyPressed(int keyCode, CallbackInfoReturnable<Boolean> callbackInfo) {
        if (validateKeyPressed(keyCode)) {
            callbackInfo.setReturnValue(Boolean.TRUE);
        }
    }

    public boolean keyPressed(int keyCode, Callable<Boolean> callable) {
        if (validateKeyPressed(keyCode)) {
            return true;
        }

        return returnCallable(callable);
    }

    private boolean validateCharTyped(char chr, boolean visible, boolean focused) {
        return Minecraft.getInstance().screen != null &&
                GUIStatus.getInstance().shouldApplyInjection() &&
                LangTypeManager.getInstance().isKorean() &&
                visible && focused &&
                StringUtil.isAllowedChatCharacter(chr) &&
                Character.charCount(chr) == 1;
    }

    public boolean charTyped(char chr, int modifiers, boolean visible, boolean focused, Callable<Boolean> callable) {
        if (!validateCharTyped(chr, visible, focused)) {
            return returnCallable(callable);
        }

        int qwertyIndex = KeyboardLayout.INSTANCE.getQwertyIndexCodePoint(chr);
        if (qwertyIndex == -1) {
            KeyboardLayout.INSTANCE.assemblePosition = -1;
            return returnCallable(callable);
        }

        char curr = KeyboardLayout.INSTANCE.layout.toCharArray()[qwertyIndex];
        if (this.getCursor() == 0 || !HangulProcessor.isHangulCharacter(curr) || !onHangulCharTyped(chr, modifiers)) {

            this.writeText(String.valueOf(HangulUtil.getFixedHangulChar(modifiers, chr, curr)));
            KeyboardLayout.INSTANCE.assemblePosition = HangulProcessor.isHangulCharacter((curr)) ? this.getCursor() : -1;
        }

        return true;
    }

    public void charTyped(char chr, int modifiers, CallbackInfoReturnable<Boolean> cir, boolean visible, boolean focused) {
        if (!validateCharTyped(chr, visible, focused)) {
            return;
        }

        int qwertyIndex = KeyboardLayout.INSTANCE.getQwertyIndexCodePoint(chr);
        if (qwertyIndex == -1) {
            KeyboardLayout.INSTANCE.assemblePosition = -1;
            return;
        }

        cir.setReturnValue(Boolean.TRUE);

        char curr = KeyboardLayout.INSTANCE.layout.toCharArray()[qwertyIndex];
        if (this.getCursor() == 0 || !HangulProcessor.isHangulCharacter(curr) || !onHangulCharTyped(chr, modifiers)) {

            this.writeText(String.valueOf(HangulUtil.getFixedHangulChar(modifiers, chr, curr)));
            KeyboardLayout.INSTANCE.assemblePosition = HangulProcessor.isHangulCharacter((curr)) ? this.getCursor() : -1;
        }
    }

    private boolean returnCallable(Callable<Boolean> callable) {
        try {
            return callable.call();
        } catch (Exception e) {
            return false;
        }
    }
}
