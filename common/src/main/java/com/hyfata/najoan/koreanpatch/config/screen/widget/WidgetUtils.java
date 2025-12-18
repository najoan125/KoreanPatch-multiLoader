package com.hyfata.najoan.koreanpatch.config.screen.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

/**
 * UI widget rendering utility
 * Helper methods for modern and consistent UI
 */
public class WidgetUtils {
    // Color constants
    public static final int COLOR_BACKGROUND = 0xFF1A1A1A;
    public static final int COLOR_WIDGET_BG = 0xFF2D2D2D;
    public static final int COLOR_WIDGET_HOVER = 0xFF3D3D3D;
    public static final int COLOR_WIDGET_ACTIVE = 0xFF4CAF50;
    public static final int COLOR_TEXT = 0xFFEEEEEE;
    public static final int COLOR_TEXT_SECONDARY = 0xFF999999;
    public static final int COLOR_BORDER = 0xFF404040;
    public static final int COLOR_ACCENT = 0xFF4CAF50;

    private static final int BORDER_RADIUS = 4;
    private static final int PADDING = 8;

    private static final Minecraft client = Minecraft.getInstance();

    /**
     * Render rounded rectangle (simple implementation)
     */
    public static void drawRoundRect(GuiGraphics guiGraphics, int x, int y, int width, int height, int color) {
        // Simple implementation - render as rectangle
        guiGraphics.fill(x, y, x + width, y + height, color);
    }

    /**
     * Render bordered rectangle
     */
    public static void drawBorderedRect(GuiGraphics guiGraphics, int x, int y, int width, int height,
                                        int fillColor, int borderColor) {
        // Background
        guiGraphics.fill(x, y, x + width, y + height, fillColor);
        // Border
        guiGraphics.fill(x, y, x + width, y + 1, borderColor); // Top
        guiGraphics.fill(x, y + height - 1, x + width, y + height, borderColor); // Bottom
        guiGraphics.fill(x, y, x + 1, y + height, borderColor); // Left
        guiGraphics.fill(x + width - 1, y, x + width, y + height, borderColor); // Right
    }

    /**
     * Render slider
     */
    public static void drawSlider(GuiGraphics guiGraphics, int x, int y, int width, int height,
                                   float value, int backgroundColor, int fillColor) {
        // Background
        drawBorderedRect(guiGraphics, x, y, width, height, backgroundColor, COLOR_BORDER);
        // Progress
        int fillWidth = (int) (width * value);
        if (fillWidth > 0) {
            guiGraphics.fill(x + 1, y + 1, x + fillWidth - 1, y + height - 1, fillColor);
        }
    }

    /**
     * Render toggle button
     */
    public static void drawToggle(GuiGraphics guiGraphics, int x, int y, boolean enabled) {
        int width = 40;
        int height = 20;
        int bgColor = enabled ? COLOR_ACCENT : COLOR_WIDGET_BG;
        drawRoundRect(guiGraphics, x, y, width, height, bgColor);

        // Circle indicator
        int circleX = enabled ? x + width - 12 : x + 4;
        guiGraphics.fill(circleX, y + 2, circleX + 16, y + 18, 0xFFFFFFFF);
    }

    /**
     * Render button
     */
    public static void drawButton(GuiGraphics guiGraphics, int x, int y, int width, int height,
                                   String text, boolean hovered, int textRenderer) {
        int bgColor = hovered ? COLOR_WIDGET_HOVER : COLOR_WIDGET_BG;
        drawBorderedRect(guiGraphics, x, y, width, height, bgColor, COLOR_BORDER);
    }

    /**
     * Render color picker box
     */
    public static void drawColorBox(GuiGraphics guiGraphics, int x, int y, int size, int color) {
        guiGraphics.fill(x, y, x + size, y + size, color);
        // Border
        guiGraphics.fill(x, y, x + size, y + 1, COLOR_BORDER);
        guiGraphics.fill(x, y + size - 1, x + size, y + size, COLOR_BORDER);
        guiGraphics.fill(x, y, x + 1, y + size, COLOR_BORDER);
        guiGraphics.fill(x + size - 1, y, x + size, y + size, COLOR_BORDER);
    }

    /**
     * Render section title
     */
    public static void drawSectionTitle(GuiGraphics guiGraphics, int x, int y, String title) {
        guiGraphics.drawString(client.font, title, x, y, COLOR_TEXT, false);
    }

    /**
     * Render label and value
     */
    public static void drawLabelValue(GuiGraphics guiGraphics, int x, int y, String label, String value) {
        guiGraphics.drawString(client.font, label, x, y, COLOR_TEXT, false);
        guiGraphics.drawString(client.font, value, x + 200, y, COLOR_TEXT_SECONDARY, false);
    }

    /**
     * Render scrollbar
     */
    public static void drawScrollbar(GuiGraphics guiGraphics, int x, int y, int height,
                                      float scrollProgress, float visibleRatio) {
        int width = 6;
        // Track background
        guiGraphics.fill(x, y, x + width, y + height, COLOR_WIDGET_BG);

        // Calculate thumb size and position
        int thumbHeight = Math.max(20, (int)(height * visibleRatio));
        int thumbY = y + (int)((height - thumbHeight) * scrollProgress);

        // Thumb
        guiGraphics.fill(x, thumbY, x + width, thumbY + thumbHeight, COLOR_ACCENT);
    }
}
