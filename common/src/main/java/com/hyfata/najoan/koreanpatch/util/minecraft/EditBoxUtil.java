package com.hyfata.najoan.koreanpatch.util.minecraft;

import com.hyfata.najoan.koreanpatch.indicator.IndicatorHandler;
import com.hyfata.najoan.koreanpatch.mixin.accessor.EditBoxAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;

public class EditBoxUtil {
    private static final Minecraft client = Minecraft.getInstance();

    public static float getCursorX(EditBox editbox) {
        EditBoxAccessor accessor = (EditBoxAccessor) editbox;
        int firstCharacterIndex = accessor.getDisplayPos();
        int selectionStart = accessor.invokeGetCursorPosition();
        String value = editbox.getValue();
        int valueLength = value.length();

        int safeFirstCharacterIndex = Math.max(0, Math.min(firstCharacterIndex, valueLength));
        int safeSelectionStart = Math.max(0, Math.min(selectionStart, valueLength));

        if (safeFirstCharacterIndex > safeSelectionStart) {
            safeFirstCharacterIndex = safeSelectionStart;
        }

        float cursorX = editbox.getX() + client.font.getSplitter().stringWidth(value.substring(safeFirstCharacterIndex, safeSelectionStart));
        float endX = editbox.getX() + editbox.getWidth() - 1.2f * IndicatorHandler.getIndicatorWidth();

        return Math.min(cursorX, endX);
    }

    public static float calculateIndicatorY(EditBox textField) {
        return textField.getY() - IndicatorHandler.getIndicatorHeight() / 1.5f;
    }

    public static float getCursorXWithText(EditBox textField, Component text, int x) {
        int textWidth = client.font.width(text);
        return Math.max(x + textWidth, getCursorX(textField));
    }
}
