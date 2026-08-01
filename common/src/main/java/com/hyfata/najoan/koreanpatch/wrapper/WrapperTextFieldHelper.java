package com.hyfata.najoan.koreanpatch.wrapper;

import com.hyfata.najoan.koreanpatch.client.GUIStatus;
import com.hyfata.najoan.koreanpatch.wrapper.handler.IMEWrapperHandler;
import com.hyfata.najoan.koreanpatch.mixin.accessor.TextFieldHelperAccessor;
import com.hyfata.najoan.koreanpatch.process.keyboard.KeyboardLayout;
import com.hyfata.najoan.koreanpatch.process.HangulProcessor;
import com.hyfata.najoan.koreanpatch.util.HangulUtil;
import com.hyfata.najoan.koreanpatch.process.LangTypeManager;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

public class WrapperTextFieldHelper implements InterfaceIMEWrapper {
    private final TextFieldHelperAccessor accessor;
    private final Minecraft client = Minecraft.getInstance();

    public WrapperTextFieldHelper(TextFieldHelperAccessor accessor) {
        this.accessor = accessor;
    }

    @Override
    public void modifyText(String str) {
        String text = this.getText();

        // insertText()
        if (accessor.readSelectionPos() != accessor.readCursorPos()) {
            text = accessor.runDeleteSelection(text);
        }

        accessor.overwriteCursorPos(Mth.clamp(accessor.readCursorPos(), 0, text.length()));
        String s = (new StringBuilder(text)).replace(accessor.readCursorPos() - 1, accessor.readCursorPos(), str).toString();
        if (accessor.getStringFilter().test(s)) {
            accessor.getStringSetter().accept(s);

            int cursorPos = Math.min(s.length(), accessor.readCursorPos() - 1 + str.length());
            accessor.overwriteCursorPos(cursorPos);
            accessor.overwriteSelectionPos(cursorPos);
        }
    }

    @Override
    public int getCursor() {
        return accessor.readSelectionPos();
    }

    @Override
    public void writeText(String str) {
        accessor.runInsert(this.getText(), str);
    }

    private boolean onBackspaceKeyPressed() {
        return IMEWrapperHandler.onBackspaceKeyPressed(this, getCursor(), this.getText());
    }

    private int getModifiers() {
        boolean shift = InputConstants.isKeyDown(client.getWindow(), GLFW.GLFW_KEY_LEFT_SHIFT) ||
                InputConstants.isKeyDown(client.getWindow(), GLFW.GLFW_KEY_RIGHT_SHIFT);
        if (shift) {
            return 1;
        }
        return 0;
    }

    private String getText() {
        return accessor.getStringGetter().get();
    }

    private void setText(String str) {
        if (accessor.getStringFilter().test(str)) {
            accessor.getStringSetter().accept(str);
        }
    }

    private boolean onHangulCharTyped(int keyCode, int modifiers) {
        String text = this.getText();
        if (text.isEmpty()) {
            return false;
        }
        return IMEWrapperHandler.onHangulCharTyped(this, keyCode, modifiers, text, accessor.selectedText(text).isEmpty());
    }

    public void deleteCharsFromCursor(CallbackInfo ci) {
        Minecraft client = Minecraft.getInstance();
        if (client.gui.screen() != null && GUIStatus.getInstance().shouldApplyInjection()) {
            if (onBackspaceKeyPressed()) {
                ci.cancel();
            }
        }
    }

    public void insertChar(char chr, CallbackInfoReturnable<Boolean> cir) {
        if (this.client.gui.screen() != null && LangTypeManager.getInstance().isKorean()) {
            cir.setReturnValue(Boolean.TRUE);
            if (chr == ' ') {
                this.writeText(String.valueOf(chr));
                KeyboardLayout.INSTANCE.assemblePosition = HangulProcessor.isHangulCharacter(chr) ? getCursor() : -1;
                return;
            }
            int qwertyIndex = KeyboardLayout.INSTANCE.getQwertyIndexCodePoint(chr);
            if (qwertyIndex == -1) {
                KeyboardLayout.INSTANCE.assemblePosition = -1;
                return;
            }
            Objects.requireNonNull(KeyboardLayout.INSTANCE);
            char curr = KeyboardLayout.INSTANCE.layout.toCharArray()[qwertyIndex];
            int cursorPosition = getCursor();
            int modifiers = this.getModifiers();
            if (cursorPosition == 0 || !HangulProcessor.isHangulCharacter(curr) || !this.onHangulCharTyped(chr, modifiers)) {

                this.writeText(String.valueOf(HangulUtil.getFixedHangulChar(modifiers, chr, curr)));
                KeyboardLayout.INSTANCE.assemblePosition = HangulProcessor.isHangulCharacter(curr) ? getCursor() : -1;
            }
        }
    }

    public void insertString(String string, CallbackInfo ci) {
        boolean colored = false;

        for (char chr : string.toCharArray()) {
            if (this.client.gui.screen() == null || !LangTypeManager.getInstance().isKorean()) continue;
            ci.cancel();
            if (chr == ' ' || chr == '\n') {
                this.writeText(String.valueOf(chr));
                KeyboardLayout.INSTANCE.assemblePosition = HangulProcessor.isHangulCharacter(chr) ? this.getCursor() : -1;
                continue;
            }
            if (chr == '§') {
                this.writeText(String.valueOf(chr));
                colored = true;
                continue;
            } else if (colored) {
                this.writeText(String.valueOf(chr));
                colored = false;
                continue;
            }
            int qwertyIndex = KeyboardLayout.INSTANCE.getQwertyIndexCodePoint(chr);
            if (qwertyIndex == -1) {
                KeyboardLayout.INSTANCE.assemblePosition = -1;
                continue;
            }
            Objects.requireNonNull(KeyboardLayout.INSTANCE);
            char curr = KeyboardLayout.INSTANCE.layout.toCharArray()[qwertyIndex];
            int cursorPosition = this.getCursor();
            int modifiers = this.getModifiers();
            if (cursorPosition != 0 && HangulProcessor.isHangulCharacter(curr) && this.onHangulCharTyped(chr, modifiers)) continue;

            this.writeText(String.valueOf(HangulUtil.getFixedHangulChar(modifiers, chr, curr)));
            KeyboardLayout.INSTANCE.assemblePosition = HangulProcessor.isHangulCharacter(curr) ? this.getCursor() : -1;
        }
    }
}