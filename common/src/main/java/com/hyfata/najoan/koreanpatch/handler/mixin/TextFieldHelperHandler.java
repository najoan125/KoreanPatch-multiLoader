package com.hyfata.najoan.koreanpatch.handler.mixin;

import com.hyfata.najoan.koreanpatch.handler.mixin.common.IMixinCommon;
import com.hyfata.najoan.koreanpatch.handler.mixin.common.MixinCommonHandler;
import com.hyfata.najoan.koreanpatch.mixin.accessor.TextFieldHelperAccessor;
import com.hyfata.najoan.koreanpatch.util.keyboard.KeyboardLayout;
import com.hyfata.najoan.koreanpatch.util.language.HangulProcessor;
import com.hyfata.najoan.koreanpatch.util.language.HangulUtil;
import com.hyfata.najoan.koreanpatch.util.language.LanguageUtil;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

public class TextFieldHelperHandler implements IMixinCommon {
    private final TextFieldHelperAccessor accessor;
    private final Minecraft client = Minecraft.getInstance();

    public TextFieldHelperHandler(TextFieldHelperAccessor accessor) {
        this.accessor = accessor;
    }

    @Override
    public void modifyText(char ch) {
        int cursorPosition = accessor.getCursor();
        char[] arr = this.getText().toCharArray();
        if (cursorPosition > 0 && cursorPosition <= arr.length) {
            arr[cursorPosition - 1] = ch;
            this.setText(String.valueOf(arr));
        }
    }

    @Override
    public int getCursor() {
        return accessor.getCursor();
    }

    @Override
    public void writeText(String str) {
        accessor.runInsert(this.getText(), str);
    }

    public boolean onBackspaceKeyPressed() {
        return MixinCommonHandler.onBackspaceKeyPressed(this, getCursor(), this.getText());
    }

    public int getModifiers() {
        boolean shift = InputConstants.isKeyDown(client.getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_SHIFT) ||
                InputConstants.isKeyDown(client.getWindow().getWindow(), GLFW.GLFW_KEY_RIGHT_SHIFT);
        if (shift) {
            return 1;
        }
        return 0;
    }

    public String getText() {
        return accessor.getStringGetter().get();
    }

    public void setText(String str) {
        if (accessor.getStringFilter().test(str)) {
            accessor.getStringSetter().accept(str);
        }
    }

    public boolean onHangulCharTyped(int keyCode, int modifiers) {
        return MixinCommonHandler.onHangulCharTyped(this, keyCode, modifiers, this.getText(), accessor.selectedText(accessor.getStringGetter().get()).isEmpty());
    }

    public void insertChar(char chr, CallbackInfoReturnable<Boolean> cir) {
        if (this.client.screen != null && LanguageUtil.isKorean()) {
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
        for (char chr : string.toCharArray()) {
            if (this.client.screen == null || !LanguageUtil.isKorean()) continue;
            ci.cancel();
            if (chr == ' ') {
                this.writeText(String.valueOf(chr));
                KeyboardLayout.INSTANCE.assemblePosition = HangulProcessor.isHangulCharacter(chr) ? this.getCursor() : -1;
                continue;
            }
            if (chr == '\n') {
                this.writeText(String.valueOf(chr));
                KeyboardLayout.INSTANCE.assemblePosition = HangulProcessor.isHangulCharacter(chr) ? this.getCursor() : -1;
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
