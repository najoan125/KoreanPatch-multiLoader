package com.hyfata.najoan.koreanpatch.util;

import com.hyfata.najoan.koreanpatch.mixin.accessor.EditBoxAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;

public class EditBoxUtil {
    private static final Minecraft client = Minecraft.getInstance();

    public static float getCursorX(EditBox textField) {
        EditBoxAccessor accessor = (EditBoxAccessor) textField;
        int firstCharacterIndex = accessor.getDisplayPos();
        int selectionStart = accessor.getCursorPos();

        float cursorX = textField.getX() + client.font.getSplitter().stringWidth(textField.getValue().substring(firstCharacterIndex, selectionStart));
        float endX = textField.getX() + textField.getWidth() - 1.2f * Indicator.getIndicatorWidth();

        return Math.min(cursorX, endX);
    }

    public static float calculateIndicatorY(EditBox textField) {
        return textField.getY() - Indicator.getIndicatorHeight() / 1.5f;
    }

    public static float getCursorXWithText(EditBox textField, Component text, int x) {
        int textWidth = client.font.width(text);
        return Math.max(x + textWidth, getCursorX(textField));
    }
}
