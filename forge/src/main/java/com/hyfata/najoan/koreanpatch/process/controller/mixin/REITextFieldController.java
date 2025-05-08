package com.hyfata.najoan.koreanpatch.process.controller.mixin;

import com.hyfata.najoan.koreanpatch.data.provider.keyboard.KeyboardLayout;
import com.hyfata.najoan.koreanpatch.process.controller.mixin.common.IMixinCommon;
import com.hyfata.najoan.koreanpatch.process.controller.mixin.common.MixinCommonController;
import com.hyfata.najoan.koreanpatch.process.handler.hangul.HangulProcessor;
import com.hyfata.najoan.koreanpatch.util.HangulUtil;
import me.shedaniel.rei.api.client.gui.widgets.TextField;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class REITextFieldController implements IMixinCommon {
    private final TextField accessor;

    public REITextFieldController(TextField accessor) {
        this.accessor = accessor;
    }

    @Override
    public int getCursor() {
        return accessor.getCursor();
    }

    public void writeText(String str) {
        accessor.addText(str);
    }

    public void modifyText(char ch) {
        String text = accessor.getText();
        text = text.substring(0, text.length() - 1) + ch;
        accessor.setText(text);
    }

    public boolean onBackspaceKeyPressed() {
        if (!accessor.getSelectedText().isEmpty()) {
            return false;
        }

        int cursorPosition = accessor.getCursor();
        return MixinCommonController.onBackspaceKeyPressed(this, cursorPosition, accessor.getText());
    }

    public boolean onHangulCharTyped(int keyCode, int modifiers) {
        return MixinCommonController.onHangulCharTyped(this, keyCode, modifiers, accessor.getText(), accessor.getSelectedText().isEmpty());
    }

    public void typedTextField(char chr, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        int qwertyIndex = KeyboardLayout.INSTANCE.getQwertyIndexCodePoint(chr);
        if (qwertyIndex == -1) {
            KeyboardLayout.INSTANCE.assemblePosition = -1;
            return;
        }

        if (accessor.m_93696_()) {
            cir.setReturnValue(Boolean.TRUE);
        } else {
            cir.setReturnValue(Boolean.FALSE);
            return;
        }

        char curr = KeyboardLayout.INSTANCE.layout.toCharArray()[qwertyIndex];
        if (this.getCursor() == 0 || !HangulProcessor.isHangulCharacter(curr) || !onHangulCharTyped(chr, modifiers)) {

            this.writeText(String.valueOf(HangulUtil.getFixedHangulChar(modifiers, chr, curr)));
            KeyboardLayout.INSTANCE.assemblePosition = HangulProcessor.isHangulCharacter((curr)) ? this.getCursor() : -1;
        }
    }
}
