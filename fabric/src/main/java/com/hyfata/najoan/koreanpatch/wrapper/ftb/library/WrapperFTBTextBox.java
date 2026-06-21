package com.hyfata.najoan.koreanpatch.wrapper.ftb.library;

import com.hyfata.najoan.koreanpatch.client.GUIStatus;
import com.hyfata.najoan.koreanpatch.mixin.accessor.CreativeModeInventoryScreenInvoker;
import com.hyfata.najoan.koreanpatch.mixin.accessor.EditBoxAccessor;
import com.hyfata.najoan.koreanpatch.process.HangulProcessor;
import com.hyfata.najoan.koreanpatch.process.LangTypeManager;
import com.hyfata.najoan.koreanpatch.process.keyboard.KeyboardLayout;
import com.hyfata.najoan.koreanpatch.util.HangulUtil;
import com.hyfata.najoan.koreanpatch.wrapper.InterfaceIMEWrapper;
import com.hyfata.najoan.koreanpatch.wrapper.handler.IMEWrapperHandler;
import dev.ftb.mods.ftblibrary.client.gui.input.Key;
import dev.ftb.mods.ftblibrary.client.gui.widget.TextBox;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.Callable;

public class WrapperFTBTextBox implements InterfaceIMEWrapper {
    private final TextBox accessor;
    private final Minecraft client = Minecraft.getInstance();

    public WrapperFTBTextBox(TextBox accessor) {
        this.accessor = accessor;
    }

    @Override
    public int getCursor() {
        return accessor.getCursorPos();
    }

    @Override
    public void writeText(String str) {
        accessor.insertText(str);
        updateScreen();
    }

    @Override
    public void modifyText(String str) {
        int cursorPosition = accessor.getCursorPos();
        accessor.setSelectionPos(cursorPosition - 1);
        accessor.insertText(str);
    }

    private void updateScreen() {
        if (this.client.screen == null) {
            return;
        }
        if (this.client.screen instanceof CreativeModeInventoryScreen && !accessor.getText().isEmpty()) {
            ((CreativeModeInventoryScreenInvoker) this.client.screen).updateCreativeSearch();
        }
    }

    private boolean onBackspaceKeyPressed() {
        if (!accessor.getSelectedText().isEmpty()) {
            return false;
        }

        int cursorPosition = accessor.getCursorPos();
        return IMEWrapperHandler.onBackspaceKeyPressed(this, cursorPosition, accessor.getText());
    }

    private boolean onHangulCharTyped(int keyCode, int modifiers) {
        return IMEWrapperHandler.onHangulCharTyped(this, keyCode, modifiers, accessor.getText(), accessor.getSelectedText().isEmpty());
    }

    private boolean validateKeyPressed(Key key) {
        Minecraft client = Minecraft.getInstance();
        if (client.screen != null &&
                GUIStatus.getInstance().shouldApplyInjection() &&
                key.event().key() == GLFW.GLFW_KEY_BACKSPACE) {
            return onBackspaceKeyPressed();
        }
        return false;
    }

    public void keyPressed(Key key, CallbackInfoReturnable<Boolean> callbackInfo) {
        if (validateKeyPressed(key)) {
            callbackInfo.setReturnValue(Boolean.TRUE);
        }
    }

    private boolean validateCharTyped(CharacterEvent event, boolean isEditable) {
        char chr = (char) event.codepoint();
        return Minecraft.getInstance().screen != null &&
                GUIStatus.getInstance().shouldApplyInjection() &&
                LangTypeManager.getInstance().isKorean() &&
                isEditable &&
                event.isAllowedChatCharacter() &&
                Character.charCount(chr) == 1;
    }

    public void charTyped(CharacterEvent charEvent, CallbackInfoReturnable<Boolean> cir, boolean isEditable) {
        char chr = (char) charEvent.codepoint();
        int modifiers = charEvent.modifiers();

        if (!validateCharTyped(charEvent, isEditable)) {
            return;
        }

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
