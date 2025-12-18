package com.hyfata.najoan.koreanpatch.config.screen.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import org.lwjgl.glfw.GLFW;

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
    public static final int COLOR_TOGGLE_OFF = 0xFF555555;
    public static final int COLOR_BUTTON_HOVER = 0xFF454545;

    // Standard Minecraft UI sizes
    public static final int STANDARD_BUTTON_WIDTH = 150;
    public static final int STANDARD_BUTTON_HEIGHT = 20;
    public static final int STANDARD_ITEM_HEIGHT = 24;
    public static final int STANDARD_TOGGLE_WIDTH = 36;
    public static final int STANDARD_TOGGLE_HEIGHT = 18;
    public static final int STANDARD_SLIDER_WIDTH = 100;
    public static final int STANDARD_SLIDER_HEIGHT = 14;

    private static final Minecraft client = Minecraft.getInstance();

    // Cursor handle cache
    private static long handCursor = 0;

    /**
     * Render rounded rectangle with corner radius
     */
    public static void drawRoundedRect(GuiGraphics guiGraphics, int x, int y, int width, int height, int color, int radius) {
        if (radius <= 0) {
            guiGraphics.fill(x, y, x + width, y + height, color);
            return;
        }

        // Clamp radius
        radius = Math.min(radius, Math.min(width / 2, height / 2));

        // Main body (excluding corners)
        guiGraphics.fill(x + radius, y, x + width - radius, y + height, color);
        guiGraphics.fill(x, y + radius, x + radius, y + height - radius, color);
        guiGraphics.fill(x + width - radius, y + radius, x + width, y + height - radius, color);

        // Draw rounded corners using filled circles
        drawFilledCorner(guiGraphics, x + radius, y + radius, radius, color, 0); // Top-left
        drawFilledCorner(guiGraphics, x + width - radius - 1, y + radius, radius, color, 1); // Top-right
        drawFilledCorner(guiGraphics, x + radius, y + height - radius - 1, radius, color, 2); // Bottom-left
        drawFilledCorner(guiGraphics, x + width - radius - 1, y + height - radius - 1, radius, color, 3); // Bottom-right
    }

    /**
     * Draw filled corner (quarter circle)
     * @param quadrant 0=top-left, 1=top-right, 2=bottom-left, 3=bottom-right
     */
    private static void drawFilledCorner(GuiGraphics guiGraphics, int centerX, int centerY, int radius, int color, int quadrant) {
        for (int dy = 0; dy <= radius; dy++) {
            for (int dx = 0; dx <= radius; dx++) {
                if (dx * dx + dy * dy <= radius * radius) {
                    int px, py;
                    switch (quadrant) {
                        case 0 -> { px = centerX - dx; py = centerY - dy; } // Top-left
                        case 1 -> { px = centerX + dx; py = centerY - dy; } // Top-right
                        case 2 -> { px = centerX - dx; py = centerY + dy; } // Bottom-left
                        case 3 -> { px = centerX + dx; py = centerY + dy; } // Bottom-right
                        default -> { px = centerX; py = centerY; }
                    }
                    guiGraphics.fill(px, py, px + 1, py + 1, color);
                }
            }
        }
    }

    /**
     * Render rounded button with hover effect
     */
    public static void drawRoundedButton(GuiGraphics guiGraphics, int x, int y, int width, int height,
                                          String text, boolean hovered, boolean pressed) {
        int bgColor;
        if (pressed) {
            bgColor = COLOR_ACCENT;
        } else if (hovered) {
            bgColor = COLOR_BUTTON_HOVER;
        } else {
            bgColor = COLOR_WIDGET_BG;
        }

        // Draw rounded background
        drawRoundedRect(guiGraphics, x, y, width, height, bgColor, 3);

        // Draw border
        drawRoundedRectBorder(guiGraphics, x, y, width, height, COLOR_BORDER, 3);

        // Draw centered text
        int textWidth = client.font.width(text);
        int textX = x + (width - textWidth) / 2;
        int textY = y + (height - client.font.lineHeight) / 2;
        guiGraphics.drawString(client.font, text, textX, textY, COLOR_TEXT, false);
    }

    /**
     * Draw rounded rectangle border only
     */
    public static void drawRoundedRectBorder(GuiGraphics guiGraphics, int x, int y, int width, int height, int color, int radius) {
        // Top edge
        guiGraphics.fill(x + radius, y, x + width - radius, y + 1, color);
        // Bottom edge
        guiGraphics.fill(x + radius, y + height - 1, x + width - radius, y + height, color);
        // Left edge
        guiGraphics.fill(x, y + radius, x + 1, y + height - radius, color);
        // Right edge
        guiGraphics.fill(x + width - 1, y + radius, x + width, y + height - radius, color);

        // Corner curves (approximate with pixels)
        if (radius > 0) {
            drawCornerBorder(guiGraphics, x + radius, y + radius, radius, color, 0);
            drawCornerBorder(guiGraphics, x + width - radius - 1, y + radius, radius, color, 1);
            drawCornerBorder(guiGraphics, x + radius, y + height - radius - 1, radius, color, 2);
            drawCornerBorder(guiGraphics, x + width - radius - 1, y + height - radius - 1, radius, color, 3);
        }
    }

    /**
     * Draw corner border arc
     */
    private static void drawCornerBorder(GuiGraphics guiGraphics, int centerX, int centerY, int radius, int color, int quadrant) {
        for (int angle = 0; angle <= 90; angle += 5) {
            double rad = Math.toRadians(angle);
            int dx = (int) Math.round(radius * Math.cos(rad));
            int dy = (int) Math.round(radius * Math.sin(rad));
            int px, py;
            switch (quadrant) {
                case 0 -> { px = centerX - dx; py = centerY - dy; }
                case 1 -> { px = centerX + dx; py = centerY - dy; }
                case 2 -> { px = centerX - dx; py = centerY + dy; }
                case 3 -> { px = centerX + dx; py = centerY + dy; }
                default -> { px = centerX; py = centerY; }
            }
            guiGraphics.fill(px, py, px + 1, py + 1, color);
        }
    }

    /**
     * Render toggle switch with sharp corners
     */
    public static void drawPillToggle(GuiGraphics guiGraphics, int x, int y, boolean enabled) {
        int width = STANDARD_TOGGLE_WIDTH;
        int height = STANDARD_TOGGLE_HEIGHT;

        // Background
        int bgColor = enabled ? COLOR_ACCENT : COLOR_TOGGLE_OFF;
        guiGraphics.fill(x, y, x + width, y + height, bgColor);

        // Knob (rectangle)
        int knobWidth = height - 4;
        int knobHeight = height - 4;
        int knobX = enabled ? x + width - knobWidth - 2 : x + 2;
        int knobY = y + 2;
        guiGraphics.fill(knobX, knobY, knobX + knobWidth, knobY + knobHeight, 0xFFFFFFFF);
    }

    /**
     * Draw pill shape (rounded ends)
     */
    public static void drawPillShape(GuiGraphics guiGraphics, int x, int y, int width, int height, int color) {
        int radius = height / 2;

        // Left semicircle
        drawFilledCircle(guiGraphics, x + radius, y + radius, radius, color);
        // Right semicircle
        drawFilledCircle(guiGraphics, x + width - radius, y + radius, radius, color);
        // Center rectangle
        guiGraphics.fill(x + radius, y, x + width - radius, y + height, color);
    }

    /**
     * Draw filled circle
     */
    public static void drawFilledCircle(GuiGraphics guiGraphics, int centerX, int centerY, int radius, int color) {
        for (int dy = -radius; dy <= radius; dy++) {
            for (int dx = -radius; dx <= radius; dx++) {
                if (dx * dx + dy * dy <= radius * radius) {
                    guiGraphics.fill(centerX + dx, centerY + dy, centerX + dx + 1, centerY + dy + 1, color);
                }
            }
        }
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
     * Render slider with sharp corners
     */
    public static void drawSlider(GuiGraphics guiGraphics, int x, int y, int width, int height,
                                   float value, int backgroundColor, int fillColor) {
        // Background track
        drawBorderedRect(guiGraphics, x, y, width, height, backgroundColor, COLOR_BORDER);

        // Progress fill
        int fillWidth = (int) (width * value);
        if (fillWidth > 0) {
            guiGraphics.fill(x + 1, y + 1, x + fillWidth - 1, y + height - 1, fillColor);
        }

        // Knob (rectangle)
        int knobWidth = 6;
        int knobX = x + (int) ((width - knobWidth) * value);
        guiGraphics.fill(knobX, y, knobX + knobWidth, y + height, 0xFFFFFFFF);
    }

    /**
     * Render toggle button (legacy, now uses pill toggle)
     */
    public static void drawToggle(GuiGraphics guiGraphics, int x, int y, boolean enabled) {
        drawPillToggle(guiGraphics, x, y, enabled);
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
     * Render section title with underline
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
     * Render scrollbar with sharp corners
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

    /**
     * Get scrollbar thumb bounds for hit testing
     */
    public static int[] getScrollbarThumbBounds(int x, int y, int height, float scrollProgress, float visibleRatio) {
        int width = 6;
        int thumbHeight = Math.max(20, (int)(height * visibleRatio));
        int thumbY = y + (int)((height - thumbHeight) * scrollProgress);
        return new int[] { x, thumbY, width, thumbHeight };
    }

    /**
     * Set hand cursor for clickable elements
     */
    public static void setHandCursor() {
        long window = client.getWindow().handle();
        if (handCursor == 0) {
            handCursor = GLFW.glfwCreateStandardCursor(GLFW.GLFW_HAND_CURSOR);
        }
        GLFW.glfwSetCursor(window, handCursor);
    }

    /**
     * Reset to default cursor
     */
    public static void setDefaultCursor() {
        long window = client.getWindow().handle();
        GLFW.glfwSetCursor(window, 0);
    }

    /**
     * Check if mouse is within bounds
     */
    public static boolean isMouseOver(double mouseX, double mouseY, int x, int y, int width, int height) {
        return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
    }
}
