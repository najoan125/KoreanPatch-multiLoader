package com.hyfata.najoan.koreanpatch.wrapper;

import com.hyfata.najoan.koreanpatch.process.keyboard.KeyboardLayout;
import com.hyfata.najoan.koreanpatch.mixin.accessor.MultilineTextFieldAccessor;
import com.hyfata.najoan.koreanpatch.wrapper.handler.IMEWrapperHandler;
import com.hyfata.najoan.koreanpatch.process.HangulProcessor;
import com.hyfata.najoan.koreanpatch.util.HangulUtil;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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
    public void modifyText(char ch) {
        accessor.invokeDeleteText(-1);
        this.writeText(String.valueOf(Character.toChars(ch)));
    }

    public boolean onBackspaceKeyPressed() {
        if (accessor.invokeHasSelection()) {
            return false;
        }

        int cursorPosition = getCursor();
        return IMEWrapperHandler.onBackspaceKeyPressed(this, cursorPosition, accessor.getValue());
    }

    public boolean onHangulCharTyped(int keyCode, int modifiers) {
        return IMEWrapperHandler.onHangulCharTyped(this, keyCode, modifiers, accessor.getValue(), !accessor.invokeHasSelection());
    }

    public void charTyped(char chr, int modifiers, CallbackInfoReturnable<Boolean> cir) {
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
}
