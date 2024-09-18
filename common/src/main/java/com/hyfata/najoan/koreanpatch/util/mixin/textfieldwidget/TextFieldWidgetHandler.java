package com.hyfata.najoan.koreanpatch.util.mixin.textfieldwidget;

import com.hyfata.najoan.koreanpatch.keyboard.KeyboardLayout;
import com.hyfata.najoan.koreanpatch.util.language.HangulProcessor;
import com.hyfata.najoan.koreanpatch.util.language.HangulUtil;
import com.hyfata.najoan.koreanpatch.util.mixin.IMixinCommon;
import com.hyfata.najoan.koreanpatch.util.mixin.MixinCommonHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class TextFieldWidgetHandler implements IMixinCommon {
    private final ITextFieldWidgetAccessor accessor;
    private final Minecraft client = Minecraft.getInstance();

    public TextFieldWidgetHandler(ITextFieldWidgetAccessor accessor) {
        this.accessor = accessor;
    }

    @Override
    public int getCursor() {
        return accessor.getCursor();
    }

    public void writeText(String str) {
        accessor.write(str);
        sendTextChanged(str);
        accessor.fabric_koreanchat$changed(accessor.getText());
        updateScreen();
    }

    private void sendTextChanged(String str) {
        if (accessor.fabric_koreanchat$getChangedListener() != null) {
            accessor.fabric_koreanchat$getChangedListener().accept(str);
        }
    }

    private void updateScreen() {
        if (this.client.screen == null) {
            return;
        }
        if (this.client.screen instanceof CreativeModeInventoryScreen && !accessor.getText().isEmpty()) {
            ((CreativeInventoryScreenInvoker) this.client.screen).updateCreativeSearch();
        }
    }

    public void modifyText(char ch) {
        int cursorPosition = accessor.getCursor();
        accessor.setCursor(cursorPosition - 1, false);
        accessor.eraseCharacters(1);
        this.writeText(String.valueOf(Character.toChars(ch)));
    }


    public boolean onBackspaceKeyPressed() {
        if (!accessor.getSelectedText().isEmpty()) {
            return false;
        }

        int cursorPosition = accessor.getCursor();
        return MixinCommonHandler.onBackspaceKeyPressed(this, cursorPosition, accessor.getText());
    }

    public boolean onHangulCharTyped(int keyCode, int modifiers) {
        return MixinCommonHandler.onHangulCharTyped(this, keyCode, modifiers, accessor.getText(), accessor.getSelectedText().isEmpty());
    }

    public void typedTextField(char chr, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        int qwertyIndex = KeyboardLayout.INSTANCE.getQwertyIndexCodePoint(chr);
        if (qwertyIndex == -1) {
            KeyboardLayout.INSTANCE.assemblePosition = -1;
            return;
        }

        if (accessor.isActive()) {
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
