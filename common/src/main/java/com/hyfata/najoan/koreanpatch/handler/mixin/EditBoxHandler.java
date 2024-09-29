package com.hyfata.najoan.koreanpatch.handler.mixin;

import com.hyfata.najoan.koreanpatch.handler.mixin.common.IMixinCommon;
import com.hyfata.najoan.koreanpatch.handler.mixin.common.MixinCommonHandler;
import com.hyfata.najoan.koreanpatch.mixin.accessor.EditBoxAccessor;
import com.hyfata.najoan.koreanpatch.util.keyboard.KeyboardLayout;
import com.hyfata.najoan.koreanpatch.mixin.accessor.CreativeModeInventoryScreenInvoker;
import com.hyfata.najoan.koreanpatch.util.language.HangulProcessor;
import com.hyfata.najoan.koreanpatch.util.language.HangulUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class EditBoxHandler implements IMixinCommon {
    private final EditBoxAccessor accessor;
    private final Minecraft client = Minecraft.getInstance();

    public EditBoxHandler(EditBoxAccessor accessor) {
        this.accessor = accessor;
    }

    @Override
    public int getCursor() {
        return accessor.invokeGetCursorPosition();
    }

    public void writeText(String str) {
        accessor.invokeInsertText(str);
        sendTextChanged(str);
        accessor.invokeChanged(accessor.invokeGetValue());
        updateScreen();
    }

    private void sendTextChanged(String str) {
        if (accessor.getResponder() != null) {
            accessor.getResponder().accept(str);
        }
    }

    private void updateScreen() {
        if (this.client.screen == null) {
            return;
        }
        if (this.client.screen instanceof CreativeModeInventoryScreen && !accessor.invokeGetValue().isEmpty()) {
            ((CreativeModeInventoryScreenInvoker) this.client.screen).updateCreativeSearch();
        }
    }

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
        return MixinCommonHandler.onBackspaceKeyPressed(this, cursorPosition, accessor.invokeGetValue());
    }

    public boolean onHangulCharTyped(int keyCode, int modifiers) {
        return MixinCommonHandler.onHangulCharTyped(this, keyCode, modifiers, accessor.invokeGetValue(), accessor.invokeGetHighlighted().isEmpty());
    }

    public void typedTextField(char chr, int modifiers, CallbackInfoReturnable<Boolean> cir) {
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
