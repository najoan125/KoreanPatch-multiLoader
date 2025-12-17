package com.hyfata.najoan.koreanpatch.config.screen.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

/**
 * UI 위젯 렌더링 유틸리티
 * 모던하고 일관된 UI를 위한 헬퍼 메서드
 */
public class WidgetUtils {
    // 색상 상수
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
     * 둥근 테두리가 있는 사각형 렌더링 (간단한 구현)
     */
    public static void drawRoundRect(GuiGraphics guiGraphics, int x, int y, int width, int height, int color) {
        // 간단한 구현 - 직사각형으로 렌더링
        guiGraphics.fill(x, y, x + width, y + height, color);
    }

    /**
     * 테두리가 있는 사각형 렌더링
     */
    public static void drawBorderedRect(GuiGraphics guiGraphics, int x, int y, int width, int height,
                                        int fillColor, int borderColor) {
        // 배경
        guiGraphics.fill(x, y, x + width, y + height, fillColor);
        // 테두리
        guiGraphics.fill(x, y, x + width, y + 1, borderColor); // 위
        guiGraphics.fill(x, y + height - 1, x + width, y + height, borderColor); // 아래
        guiGraphics.fill(x, y, x + 1, y + height, borderColor); // 왼쪽
        guiGraphics.fill(x + width - 1, y, x + width, y + height, borderColor); // 오른쪽
    }

    /**
     * 슬라이더 렌더링
     */
    public static void drawSlider(GuiGraphics guiGraphics, int x, int y, int width, int height,
                                   float value, int backgroundColor, int fillColor) {
        // 배경
        drawBorderedRect(guiGraphics, x, y, width, height, backgroundColor, COLOR_BORDER);
        // 진행 상황
        int fillWidth = (int) (width * value);
        if (fillWidth > 0) {
            guiGraphics.fill(x + 1, y + 1, x + fillWidth - 1, y + height - 1, fillColor);
        }
    }

    /**
     * 토글 버튼 렌더링
     */
    public static void drawToggle(GuiGraphics guiGraphics, int x, int y, boolean enabled) {
        int width = 40;
        int height = 20;
        int bgColor = enabled ? COLOR_ACCENT : COLOR_WIDGET_BG;
        drawRoundRect(guiGraphics, x, y, width, height, bgColor);

        // 원형 인디케이터
        int circleX = enabled ? x + width - 12 : x + 4;
        guiGraphics.fill(circleX, y + 2, circleX + 16, y + 18, 0xFFFFFFFF);
    }

    /**
     * 버튼 렌더링
     */
    public static void drawButton(GuiGraphics guiGraphics, int x, int y, int width, int height,
                                   String text, boolean hovered, int textRenderer) {
        int bgColor = hovered ? COLOR_WIDGET_HOVER : COLOR_WIDGET_BG;
        drawBorderedRect(guiGraphics, x, y, width, height, bgColor, COLOR_BORDER);
    }

    /**
     * 색상 선택기 박스 렌더링
     */
    public static void drawColorBox(GuiGraphics guiGraphics, int x, int y, int size, int color) {
        guiGraphics.fill(x, y, x + size, y + size, color);
        // 테두리
        guiGraphics.fill(x, y, x + size, y + 1, COLOR_BORDER);
        guiGraphics.fill(x, y + size - 1, x + size, y + size, COLOR_BORDER);
        guiGraphics.fill(x, y, x + 1, y + size, COLOR_BORDER);
        guiGraphics.fill(x + size - 1, y, x + size, y + size, COLOR_BORDER);
    }

    /**
     * 섹션 제목 렌더링
     */
    public static void drawSectionTitle(GuiGraphics guiGraphics, int x, int y, String title) {
        guiGraphics.drawString(client.font, title, x, y, COLOR_TEXT, false);
    }

    /**
     * 라벨과 값 렌더링
     */
    public static void drawLabelValue(GuiGraphics guiGraphics, int x, int y, String label, String value) {
        guiGraphics.drawString(client.font, label, x, y, COLOR_TEXT, false);
        guiGraphics.drawString(client.font, value, x + 200, y, COLOR_TEXT_SECONDARY, false);
    }
}
