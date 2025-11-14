package com.hyfata.najoan.koreanpatch.wrapper;

import com.hyfata.najoan.koreanpatch.wrapper.handler.IMEWrapperHandler;
import com.hyfata.najoan.koreanpatch.process.keyboard.KeyboardLayout;
import com.hyfata.najoan.koreanpatch.process.HangulProcessor;
import com.hyfata.najoan.koreanpatch.util.HangulUtil;
import me.shedaniel.rei.api.client.gui.widgets.TextField;
import me.shedaniel.rei.impl.client.gui.widget.basewidgets.TextFieldWidget;
import net.minecraft.SharedConstants;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class WrapperREITextField implements InterfaceIMEWrapper {
    private final TextFieldWidget accessor;

    public WrapperREITextField(TextFieldWidget accessor) {
        this.accessor = accessor;
    }

    @Override
    public int getCursor() {
        return accessor.getCursor();
    }

    public void writeText(String str) {
        accessor.addText(str);
    }

    @Override
    public void modifyText(String str) {
        int cursorPosition = accessor.getCursor();

        // addText()
        int highlightStart = cursorPosition - 1;
        int highlightEnd = cursorPosition;
        int k = accessor.getMaxLength() - accessor.getText().length() - (highlightStart - highlightEnd);
        String textFiltered = SharedConstants.filterText(str);
        int l = textFiltered.length();
        if (k < l) {
            textFiltered = textFiltered.substring(0, k);
            l = k;
        }

        String result = (new StringBuilder(accessor.getText())).replace(highlightStart, highlightEnd, textFiltered).toString();
        accessor.setText(result);
        accessor.setCursorPosition(highlightStart + l);
        accessor.setHighlightPos(accessor.getCursor());
        accessor.onChanged(result);
    }

    public boolean onBackspaceKeyPressed() {
        if (!accessor.getSelectedText().isEmpty()) {
            return false;
        }

        int cursorPosition = accessor.getCursor();
        return IMEWrapperHandler.onBackspaceKeyPressed(this, cursorPosition, accessor.getText());
    }

    public boolean onHangulCharTyped(int keyCode, int modifiers) {
        return IMEWrapperHandler.onHangulCharTyped(this, keyCode, modifiers, accessor.getText(), accessor.getSelectedText().isEmpty());
    }

    public void typedTextField(char chr, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        int qwertyIndex = KeyboardLayout.INSTANCE.getQwertyIndexCodePoint(chr);
        if (qwertyIndex == -1) {
            KeyboardLayout.INSTANCE.assemblePosition = -1;
            return;
        }

        if (accessor.isFocused()) {
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
