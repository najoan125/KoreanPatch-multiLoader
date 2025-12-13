package com.hyfata.najoan.koreanpatch.wrapper;

import com.hyfata.najoan.koreanpatch.client.GUIStatus;
import com.hyfata.najoan.koreanpatch.process.LangTypeManager;
import com.hyfata.najoan.koreanpatch.wrapper.handler.IMEWrapperHandler;
import com.hyfata.najoan.koreanpatch.mixin.accessor.EditBoxAccessor;
import com.hyfata.najoan.koreanpatch.process.keyboard.KeyboardLayout;
import com.hyfata.najoan.koreanpatch.mixin.accessor.CreativeModeInventoryScreenInvoker;
import com.hyfata.najoan.koreanpatch.process.HangulProcessor;
import com.hyfata.najoan.koreanpatch.util.HangulUtil;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.Callable;

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

    @Override
    public void modifyText(String str) {
        int cursorPosition = accessor.invokeGetCursorPosition();
        accessor.invokeSetHighlightPos(cursorPosition - 1);
        accessor.invokeInsertText(str);
    }

    private void updateScreen() {
        if (this.client.screen == null) {
            return;
        }
        if (this.client.screen instanceof CreativeModeInventoryScreen && !accessor.invokeGetValue().isEmpty()) {
            ((CreativeModeInventoryScreenInvoker) this.client.screen).updateCreativeSearch();
        }
    }

    private boolean onBackspaceKeyPressed() {
        if (!accessor.invokeGetHighlighted().isEmpty()) {
            return false;
        }

        int cursorPosition = accessor.invokeGetCursorPosition();
        return IMEWrapperHandler.onBackspaceKeyPressed(this, cursorPosition, accessor.invokeGetValue());
    }

    private boolean onHangulCharTyped(int keyCode, int modifiers) {
        return IMEWrapperHandler.onHangulCharTyped(this, keyCode, modifiers, accessor.invokeGetValue(), accessor.invokeGetHighlighted().isEmpty());
    }

    private boolean validateKeyPressed(int keyCode) {
        Minecraft client = Minecraft.getInstance();
        if (client.screen != null &&
                !GUIStatus.getInstance().isBypassInjection() &&
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

    private boolean validateCharTyped(char chr, boolean isEditable) {
        return Minecraft.getInstance().screen != null &&
                !GUIStatus.getInstance().isBypassInjection() &&
                LangTypeManager.getInstance().isKorean() &&
                isEditable &&
                SharedConstants.isAllowedChatCharacter(chr) &&
                Character.charCount(chr) == 1;
    }

    public boolean charTyped(char chr, int modifiers, boolean isEditable, Callable<Boolean> callable) {
        if (!validateCharTyped(chr, isEditable)) {
            return returnCallable(callable);
        }

        int qwertyIndex = KeyboardLayout.INSTANCE.getQwertyIndexCodePoint(chr);
        if (qwertyIndex == -1) {
            KeyboardLayout.INSTANCE.assemblePosition = -1;
            return returnCallable(callable);
        }

        if (!accessor.invokeCanConsumeInput()) {
            return false;
        }

        char curr = KeyboardLayout.INSTANCE.layout.toCharArray()[qwertyIndex];
        if (this.getCursor() == 0 || !HangulProcessor.isHangulCharacter(curr) || !onHangulCharTyped(chr, modifiers)) {

            this.writeText(String.valueOf(HangulUtil.getFixedHangulChar(modifiers, chr, curr)));
            KeyboardLayout.INSTANCE.assemblePosition = HangulProcessor.isHangulCharacter((curr)) ? this.getCursor() : -1;
        }

        return true;
    }

    public void charTyped(char chr, int modifiers, CallbackInfoReturnable<Boolean> cir, boolean isEditable) {
        if (!validateCharTyped(chr, isEditable)) {
            return;
        }

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

    private boolean returnCallable(Callable<Boolean> callable) {
        try {
            return callable.call();
        } catch (Exception e) {
            return false;
        }
    }
}
