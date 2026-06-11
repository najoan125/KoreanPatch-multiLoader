package com.hyfata.najoan.koreanpatch.config.screen.tab;

import com.hyfata.najoan.koreanpatch.config.screen.widget.WidgetUtils;
import net.minecraft.client.gui.GuiGraphics;

/**
 * 설정 탭 내의 스크롤 동작을 캡슐화합니다.
 * Encapsulates scrolling behavior inside a settings tab.
 */
public class TabScrollHandler {
    private int scrollOffset = 0;
    private boolean isDraggingScrollbar = false;

    private final int contentStartY;
    private final int contentHeight;
    private final int contentWidth;

    private static final int SCROLL_STEP = 15;

    public TabScrollHandler(int contentWidth, int contentStartY, int contentHeight) {
        this.contentWidth = contentWidth;
        this.contentStartY = contentStartY;
        this.contentHeight = contentHeight;
    }

    public void renderScrollbar(GuiGraphics guiGraphics, int totalContentHeight) {
        int scrollbarHeight = contentHeight - contentStartY;
        float visibleRatio = (float) (contentHeight - contentStartY) / totalContentHeight;
        int maxScroll = Math.max(0, totalContentHeight - (contentHeight - contentStartY));
        float scrollProgress = maxScroll > 0 ? (float) scrollOffset / maxScroll : 0;

        if (visibleRatio < 1.0f) {
            WidgetUtils.drawScrollbar(guiGraphics, contentWidth - 10, contentStartY, scrollbarHeight,
                    scrollProgress, visibleRatio);
        }
    }

    public boolean handleMouseClicked(double mouseX, double mouseY, int totalContentHeight) {
        int scrollbarHeight = contentHeight - contentStartY;
        float visibleRatio = (float) (contentHeight - contentStartY) / totalContentHeight;
        if (visibleRatio < 1.0f) {
            int maxScroll = Math.max(0, totalContentHeight - (contentHeight - contentStartY));
            float scrollProgress = maxScroll > 0 ? (float) scrollOffset / maxScroll : 0;
            int[] thumbBounds = WidgetUtils.getScrollbarThumbBounds(contentWidth - 10, contentStartY, scrollbarHeight,
                    scrollProgress, visibleRatio);

            if (WidgetUtils.isMouseOver(mouseX, mouseY, thumbBounds[0], thumbBounds[1], thumbBounds[2], thumbBounds[3])) {
                isDraggingScrollbar = true;
                return true;
            }
        }
        return false;
    }

    public boolean handleMouseDragged(double mouseY, int totalContentHeight) {
        if (isDraggingScrollbar) {
            int scrollbarHeight = contentHeight - contentStartY;
            float visibleRatio = (float) (contentHeight - contentStartY) / totalContentHeight;
            int thumbHeight = Math.max(20, (int) (scrollbarHeight * visibleRatio));
            int maxScroll = Math.max(0, totalContentHeight - (contentHeight - contentStartY));

            // thumb 중심이 마우스를 따라가도록 비율로 변환
            // Convert mouse position to a ratio so the thumb center follows the cursor.
            float progress = (float) (mouseY - contentStartY - thumbHeight / 2) / (scrollbarHeight - thumbHeight);
            progress = Math.max(0, Math.min(1, progress));
            scrollOffset = (int) (progress * maxScroll);
            return true;
        }
        return false;
    }

    public boolean handleMouseReleased() {
        if (isDraggingScrollbar) {
            isDraggingScrollbar = false;
            return true;
        }
        return false;
    }

    public boolean handleMouseScrolled(double scrollY, int totalContentHeight) {
        int maxScroll = totalContentHeight - (contentHeight - contentStartY);
        maxScroll = Math.max(0, maxScroll);
        this.scrollOffset = (int) Math.max(0, Math.min(maxScroll, this.scrollOffset - scrollY * SCROLL_STEP));
        return true;
    }

    public int getScrollOffset() {
        return scrollOffset;
    }
}
