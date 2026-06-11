package com.hyfata.najoan.koreanpatch.config.screen.tab;

import com.hyfata.najoan.koreanpatch.config.screen.widget.WidgetUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

import java.util.List;
import java.util.function.Consumer;

/**
 * 설정 탭의 공통 UI 렌더링 유틸리티.
 * Shared UI rendering utilities for settings tabs.
 *
 * <p>모든 draw* 메서드는 렌더링 후 <b>다음 Y 위치</b>를 반환합니다.
 * All draw* methods return the <b>next Y position</b> after rendering.</p>
 */
public class TabRenderHelper {
    private static final Minecraft CLIENT = Minecraft.getInstance();
    public static final int SECTION_SPACING = 25;
    public static final int SECTION_GAP = 15;
    public static final int ITEM_HEIGHT = 22;
    public static final int CONTENT_WIDTH = 350;
    public static final int WIDGET_OFFSET_X = 200;
    public static final int ITEM_GAP = 8;

    /**
     * 클릭 가능한 위젯 영역. 매 프레임 render()에서 clear 후 재구축됨.
     * Clickable widget bounds. Rebuilt from scratch every render() frame.
     */
    public record ClickableWidget(int x, int y, int width, int height, WidgetType type, Runnable onClick, SliderHandler sliderHandler) {
        public boolean contains(double mx, double my) {
            return mx >= x && mx < x + width && my >= y && my < y + height;
        }
    }

    public enum WidgetType { TOGGLE, SLIDER, ENUM, COLOR }

    @FunctionalInterface
    public interface SliderHandler {
        void onDrag(float value);
    }

    public static int contentX(int contentWidth) {
        return (contentWidth - CONTENT_WIDTH) / 2;
    }

    public static int startY(int contentStartY, int scrollOffset) {
        return contentStartY + 15 - scrollOffset;
    }

    public static int drawSection(GuiGraphics guiGraphics, int x, int y, String title, int width) {
        WidgetUtils.drawSectionTitle(guiGraphics, x, y, title);
        guiGraphics.fill(x, y + 14, x + width, y + 15, WidgetUtils.COLOR_BORDER);
        return y + SECTION_SPACING;
    }

    public static int drawToggleSetting(GuiGraphics guiGraphics, int x, int y, String label, boolean value, Runnable onClick, List<ClickableWidget> widgets) {
        int labelY = y + (WidgetUtils.STANDARD_TOGGLE_HEIGHT - CLIENT.font.lineHeight) / 2;
        guiGraphics.drawString(CLIENT.font, label, x, labelY, WidgetUtils.COLOR_TEXT, false);

        int toggleX = x + WIDGET_OFFSET_X;
        WidgetUtils.drawToggle(guiGraphics, toggleX, y, value);
        widgets.add(new ClickableWidget(toggleX, y, WidgetUtils.STANDARD_TOGGLE_WIDTH, WidgetUtils.STANDARD_TOGGLE_HEIGHT, WidgetType.TOGGLE, onClick, null));
        return y + ITEM_HEIGHT + ITEM_GAP;
    }

    public static int drawEnumSetting(GuiGraphics guiGraphics, int x, int y, String label, String value, Runnable onClick, List<ClickableWidget> widgets) {
        int labelY = y + (20 - CLIENT.font.lineHeight) / 2;
        guiGraphics.drawString(CLIENT.font, label, x, labelY, WidgetUtils.COLOR_TEXT, false);

        int enumX = x + WIDGET_OFFSET_X;
        int enumWidth = 100;
        WidgetUtils.drawBorderedRect(guiGraphics, enumX, y, enumWidth, 20, WidgetUtils.COLOR_WIDGET_BG, WidgetUtils.COLOR_BORDER);

        int textY = y + (20 - CLIENT.font.lineHeight) / 2;
        guiGraphics.drawString(CLIENT.font, value, enumX + 8, textY, WidgetUtils.COLOR_TEXT_SECONDARY, false);
        widgets.add(new ClickableWidget(enumX, y, enumWidth, 20, WidgetType.ENUM, onClick, null));
        return y + ITEM_HEIGHT + ITEM_GAP;
    }

    public static int drawSliderSetting(GuiGraphics guiGraphics, int x, int y, String label, float value, SliderHandler handler, List<ClickableWidget> widgets) {
        int labelY = y + (WidgetUtils.STANDARD_SLIDER_HEIGHT - CLIENT.font.lineHeight) / 2;
        guiGraphics.drawString(CLIENT.font, label, x, labelY, WidgetUtils.COLOR_TEXT, false);

        int sliderX = x + WIDGET_OFFSET_X;
        WidgetUtils.drawSlider(guiGraphics, sliderX, y, WidgetUtils.STANDARD_SLIDER_WIDTH, WidgetUtils.STANDARD_SLIDER_HEIGHT,
                value, WidgetUtils.COLOR_WIDGET_BG, WidgetUtils.COLOR_ACCENT);

        String percentText = String.format("%.0f%%", value * 100);
        guiGraphics.drawString(CLIENT.font, percentText, sliderX + WidgetUtils.STANDARD_SLIDER_WIDTH + 8, labelY, WidgetUtils.COLOR_TEXT_SECONDARY, false);
        widgets.add(new ClickableWidget(sliderX, y, WidgetUtils.STANDARD_SLIDER_WIDTH, WidgetUtils.STANDARD_SLIDER_HEIGHT, WidgetType.SLIDER, null, handler));
        return y + ITEM_HEIGHT + ITEM_GAP;
    }

    public static int drawColorSetting(GuiGraphics guiGraphics, int x, int y, String label, int color) {
        int labelY = y + (20 - CLIENT.font.lineHeight) / 2;
        guiGraphics.drawString(CLIENT.font, label, x, labelY, WidgetUtils.COLOR_TEXT, false);
        WidgetUtils.drawColorBox(guiGraphics, x + WIDGET_OFFSET_X, y, 20, color);
        return y + ITEM_HEIGHT + ITEM_GAP;
    }

    /**
     * 클릭한 위젯을 찾아 상호작용을 실행합니다. slider인 경우 onSliderDragStart 콜백도 호출됩니다.
     * Finds the clicked widget and runs its interaction. For sliders, onSliderDragStart is also invoked.
     */
    public static boolean handleWidgetClick(List<ClickableWidget> widgets, double mouseX, double mouseY, Consumer<ClickableWidget> onSliderDragStart) {
        for (ClickableWidget widget : widgets) {
            if (widget.contains(mouseX, mouseY)) {
                if (widget.type == WidgetType.SLIDER && widget.sliderHandler != null) {
                    float value = (float) Math.max(0, Math.min(1, (mouseX - widget.x) / widget.width));
                    widget.sliderHandler.onDrag(value);
                    if (onSliderDragStart != null) {
                        onSliderDragStart.accept(widget);
                    }
                    return true;
                } else if (widget.onClick != null) {
                    widget.onClick.run();
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean handleSliderDrag(ClickableWidget draggingSlider, double mouseX) {
        if (draggingSlider != null && draggingSlider.sliderHandler != null) {
            float value = (float) Math.max(0, Math.min(1, (mouseX - draggingSlider.x) / draggingSlider.width));
            draggingSlider.sliderHandler.onDrag(value);
            return true;
        }
        return false;
    }
}
