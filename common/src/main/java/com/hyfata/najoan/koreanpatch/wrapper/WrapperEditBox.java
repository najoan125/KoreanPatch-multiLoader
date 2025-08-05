package com.hyfata.najoan.koreanpatch.wrapper;

import com.hyfata.najoan.koreanpatch.wrapper.handler.IMEWrapperHandler;
import com.hyfata.najoan.koreanpatch.mixin.accessor.EditBoxAccessor;
import com.hyfata.najoan.koreanpatch.process.keyboard.KeyboardLayout;
import com.hyfata.najoan.koreanpatch.mixin.accessor.CreativeModeInventoryScreenInvoker;
import com.hyfata.najoan.koreanpatch.process.HangulProcessor;
import com.hyfata.najoan.koreanpatch.util.HangulUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class WrapperEditBox implements InterfaceIMEWrapper {
    private final EditBoxAccessor accessor;
    private final Minecraft client = Minecraft.getInstance();

    public WrapperEditBox(EditBoxAccessor accessor) {
        this.accessor = accessor;
    }

    @Override
    public int getCursor() {
        return accessor.invokeGetCursorPosition();
    }

    @Override
    public void writeText(String str) {
        accessor.invokeInsertText(str);
        updateScreen();
    }

    private void updateScreen() {
        if (this.client.screen == null) {
            return;
        }
        if (this.client.screen instanceof CreativeModeInventoryScreen && !accessor.invokeGetValue().isEmpty()) {
            ((CreativeModeInventoryScreenInvoker) this.client.screen).updateCreativeSearch();
        }
    }

    @Override
    public void modifyText(char ch) {
        int cursorPosition = accessor.invokeGetCursorPosition();
        accessor.invokeMoveCursorTo(cursorPosition - 1, false);
        accessor.invokeDeleteChars(1);
        this.writeText(String.valueOf(Character.toChars(ch)));
    }

    public boolean onBackspaceKeyPressed() {
        if (!accessor.invokeGetHighlighted().isEmpty()) {
            return false;
        }

        int cursorPosition = accessor.invokeGetCursorPosition();
        return IMEWrapperHandler.onBackspaceKeyPressed(this, cursorPosition, accessor.invokeGetValue());
    }

    public boolean onHangulCharTyped(int keyCode, int modifiers) {
        return IMEWrapperHandler.onHangulCharTyped(this, keyCode, modifiers, accessor.invokeGetValue(), accessor.invokeGetHighlighted().isEmpty());
    }

    public void charTyped(char chr, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        int qwertyIndex = KeyboardLayout.INSTANCE.getQwertyIndexCodePoint(chr);
        if (qwertyIndex == -1) {
            KeyboardLayout.INSTANCE.assemblePosition = -1;
            return;
        }

        if (accessor.invokeCanConsumeInput()) {
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
