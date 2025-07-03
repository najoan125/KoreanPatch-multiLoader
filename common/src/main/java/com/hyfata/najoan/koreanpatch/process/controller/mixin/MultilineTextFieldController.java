package com.hyfata.najoan.koreanpatch.process.controller.mixin;

import com.hyfata.najoan.koreanpatch.data.provider.keyboard.KeyboardLayout;
import com.hyfata.najoan.koreanpatch.mixin.accessor.MultilineTextFieldAccessor;
import com.hyfata.najoan.koreanpatch.process.controller.mixin.common.IMixinCommon;
import com.hyfata.najoan.koreanpatch.process.controller.mixin.common.MixinCommonController;
import com.hyfata.najoan.koreanpatch.process.handler.hangul.HangulProcessor;
import com.hyfata.najoan.koreanpatch.util.HangulUtil;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class MultilineTextFieldController implements IMixinCommon {
    private final MultilineTextFieldAccessor accessor;

    public MultilineTextFieldController(MultilineTextFieldAccessor accessor) {
        this.accessor = accessor;
    }

    @Override
    public int getCursor() {
        return accessor.getCursor();
    }

    public void writeText(String str) {
        accessor.invokeInsertText(str);
    }

    public void modifyText(char ch) {
        accessor.invokeDeleteText(-1);
        this.writeText(String.valueOf(Character.toChars(ch)));
    }


    public boolean onBackspaceKeyPressed() {
        if (accessor.invokeHasSelection()) {
            return false;
        }

        int cursorPosition = getCursor();
        return MixinCommonController.onBackspaceKeyPressed(this, cursorPosition, accessor.getValue());
    }

    public boolean onHangulCharTyped(int keyCode, int modifiers) {
        return MixinCommonController.onHangulCharTyped(this, keyCode, modifiers, accessor.getValue(), !accessor.invokeHasSelection());
    }

    public void typedTextField(char chr, int modifiers, CallbackInfoReturnable<Boolean> cir) {
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
