package com.hyfata.najoan.koreanpatch.config.screen.tab;

import com.hyfata.najoan.koreanpatch.config.ConfigManager;
import com.hyfata.najoan.koreanpatch.config.EasingFunctions;
import com.hyfata.najoan.koreanpatch.config.OutlineType;
import com.hyfata.najoan.koreanpatch.config.category.CategoryIndicator;
import com.hyfata.najoan.koreanpatch.config.screen.widget.WidgetUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Indicator settings tab
 * Configure appearance and behavior of language indicator
 */
public class IndicatorSettingsTab extends SettingsTab {
    private CategoryIndicator config;
    private int scrollOffset = 0;
    private static final int SCROLL_STEP = 15;
    private static final int SECTION_SPACING = 25;  // Space after section title (title height + gap to first item)
    private static final int SECTION_GAP = 15;      // Gap between sections (after last item of previous section)
    private static final int ITEM_HEIGHT = 22;
    private static final int CONTENT_WIDTH = 350;
    private static final int WIDGET_OFFSET_X = 200;

    // Track clickable widgets
    private final List<ClickableWidget> clickableWidgets = new ArrayList<>();
    private ClickableWidget draggingSlider = null;

    // Scrollbar drag state
    private boolean isDraggingScrollbar = false;
    private int scrollbarX, scrollbarY, scrollbarHeight;

    private static final Minecraft client = Minecraft.getInstance();

    // Clickable widget information
    private record ClickableWidget(int x, int y, int width, int height, WidgetType type, Runnable onClick, SliderHandler sliderHandler) {
        boolean contains(double mx, double my) {
            return mx >= x && mx < x + width && my >= y && my < y + height;
        }
    }

    private enum WidgetType { TOGGLE, SLIDER, ENUM, COLOR }

    @FunctionalInterface
    private interface SliderHandler {
        void onDrag(float value);
    }

    @Override
    public Component getTabName() {
        return Component.translatable("koreanpatch.config.tab.indicator");
    }

    @Override
    protected void onInit() {
        this.config = ConfigManager.getInstance().getConfig().getCategoryIndicator();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        // Clear widget list every frame
        clickableWidgets.clear();

        int padding = 15;
        int contentX = (contentWidth - CONTENT_WIDTH) / 2; // Center content
        int y = contentStartY + padding - scrollOffset;

        // General settings section
        drawSection(guiGraphics, contentX, y, Component.translatable("koreanpatch.config.indicator.general").getString(), CONTENT_WIDTH);
        y += SECTION_SPACING;

        // Show indicator toggle
        drawToggleSetting(guiGraphics, contentX, y,
                Component.translatable("koreanpatch.config.indicator.general.show").getString(),
                config.isShowIndicator(),
                () -> config.setShowIndicator(!config.isShowIndicator()));
        y += ITEM_HEIGHT + 8;

        y += SECTION_GAP;

        // Outline settings section
        drawSection(guiGraphics, contentX, y, Component.translatable("koreanpatch.config.indicator.outline").getString(), CONTENT_WIDTH);
        y += SECTION_SPACING;

        drawToggleSetting(guiGraphics, contentX, y,
                Component.translatable("koreanpatch.config.indicator.outline.show").getString(),
                config.getOutlineSettings().isShowOutline(),
                () -> config.getOutlineSettings().setShowOutline(!config.getOutlineSettings().isShowOutline()));
        y += ITEM_HEIGHT + 8;

        drawEnumSetting(guiGraphics, contentX, y,
                Component.translatable("koreanpatch.config.indicator.outline.outline_type").getString(),
                config.getOutlineSettings().getOutlineType().name(),
                () -> {
                    OutlineType[] values = OutlineType.values();
                    int next = (config.getOutlineSettings().getOutlineType().ordinal() + 1) % values.length;
                    config.getOutlineSettings().setOutlineType(values[next]);
                });
        y += ITEM_HEIGHT + 8;

        drawColorSetting(guiGraphics, contentX, y,
                Component.translatable("koreanpatch.config.color_opacity.ko").getString(),
                config.getOutlineSettings().getColorOpacitySettings().getKoreanColor().getRGB());
        y += ITEM_HEIGHT + 8;

        drawColorSetting(guiGraphics, contentX, y,
                Component.translatable("koreanpatch.config.color_opacity.en").getString(),
                config.getOutlineSettings().getColorOpacitySettings().getEnColor().getRGB());
        y += ITEM_HEIGHT + 8;

        drawSliderSetting(guiGraphics, contentX, y,
                Component.translatable("koreanpatch.config.color_opacity.opacity").getString(),
                config.getOutlineSettings().getColorOpacitySettings().getOpacity() / 100f,
                v -> config.getOutlineSettings().getColorOpacitySettings().setOpacity((int) (v * 100)));
        y += ITEM_HEIGHT + 8;

        y += SECTION_GAP;

        // Background settings section
        drawSection(guiGraphics, contentX, y, Component.translatable("koreanpatch.config.indicator.background").getString(), CONTENT_WIDTH);
        y += SECTION_SPACING;

        drawColorSetting(guiGraphics, contentX, y,
                Component.translatable("koreanpatch.config.color_opacity.ko").getString(),
                config.getBackgroundSettings().getKoreanColor().getRGB());
        y += ITEM_HEIGHT + 8;

        drawColorSetting(guiGraphics, contentX, y,
                Component.translatable("koreanpatch.config.color_opacity.en").getString(),
                config.getBackgroundSettings().getEnColor().getRGB());
        y += ITEM_HEIGHT + 8;

        drawSliderSetting(guiGraphics, contentX, y,
                Component.translatable("koreanpatch.config.color_opacity.opacity").getString(),
                config.getBackgroundSettings().getOpacity() / 100f,
                v -> config.getBackgroundSettings().setOpacity((int) (v * 100)));
        y += ITEM_HEIGHT + 8;

        y += SECTION_GAP;

        // Text settings section
        drawSection(guiGraphics, contentX, y, Component.translatable("koreanpatch.config.indicator.text").getString(), CONTENT_WIDTH);
        y += SECTION_SPACING;

        drawColorSetting(guiGraphics, contentX, y,
                Component.translatable("koreanpatch.config.color_opacity.ko").getString(),
                config.getTextSettings().getKoreanColor().getRGB());
        y += ITEM_HEIGHT + 8;

        drawColorSetting(guiGraphics, contentX, y,
                Component.translatable("koreanpatch.config.color_opacity.en").getString(),
                config.getTextSettings().getEnColor().getRGB());
        y += ITEM_HEIGHT + 8;

        drawSliderSetting(guiGraphics, contentX, y,
                Component.translatable("koreanpatch.config.color_opacity.opacity").getString(),
                config.getTextSettings().getOpacity() / 100f,
                v -> config.getTextSettings().setOpacity((int) (v * 100)));
        y += ITEM_HEIGHT + 8;

        y += SECTION_GAP;

        // Animation settings section
        drawSection(guiGraphics, contentX, y, Component.translatable("koreanpatch.config.indicator.animation").getString(), CONTENT_WIDTH);
        y += SECTION_SPACING;

        drawToggleSetting(guiGraphics, contentX, y,
                Component.translatable("koreanpatch.config.indicator.animation.show").getString(),
                config.getAnimationSettings().isShowAnimation(),
                () -> config.getAnimationSettings().setShowAnimation(!config.getAnimationSettings().isShowAnimation()));
        y += ITEM_HEIGHT + 8;

        drawEnumSetting(guiGraphics, contentX, y,
                Component.translatable("koreanpatch.config.indicator.animation.easing_function").getString(),
                config.getAnimationSettings().getEasingFunction().name(),
                () -> {
                    EasingFunctions[] values = EasingFunctions.values();
                    int next = (config.getAnimationSettings().getEasingFunction().ordinal() + 1) % values.length;
                    config.getAnimationSettings().setEasingFunction(values[next]);
                });
        y += ITEM_HEIGHT + 8;

        drawSliderSetting(guiGraphics, contentX, y,
                Component.translatable("koreanpatch.config.indicator.animation.speed").getString(),
                config.getAnimationSettings().getSpeed() / 100f,
                v -> config.getAnimationSettings().setSpeed((int) (v * 100)));

        // Draw scrollbar
        scrollbarX = contentWidth - 10;
        scrollbarY = contentStartY;
        scrollbarHeight = contentHeight - contentStartY;
        int totalContentHeight = getContentHeight();
        float visibleRatio = (float)(contentHeight - contentStartY) / totalContentHeight;
        int maxScroll = Math.max(0, totalContentHeight - (contentHeight - contentStartY));
        float scrollProgress = maxScroll > 0 ? (float)scrollOffset / maxScroll : 0;

        if (visibleRatio < 1.0f) {
            WidgetUtils.drawScrollbar(guiGraphics, scrollbarX, scrollbarY, scrollbarHeight,
                    scrollProgress, visibleRatio);
        }
    }

    /**
     * Calculate total content height
     */
    private int getContentHeight() {
        int height = 15;
        // General settings: section + 1 toggle + section gap
        height += SECTION_SPACING + (ITEM_HEIGHT + 8) + SECTION_GAP;
        // Outline settings: section + 5 items + section gap
        height += SECTION_SPACING + (ITEM_HEIGHT + 8) * 5 + SECTION_GAP;
        // Background settings: section + 3 items + section gap
        height += SECTION_SPACING + (ITEM_HEIGHT + 8) * 3 + SECTION_GAP;
        // Text settings: section + 3 items + section gap
        height += SECTION_SPACING + (ITEM_HEIGHT + 8) * 3 + SECTION_GAP;
        // Animation settings: section + 3 items (no gap after last section)
        height += SECTION_SPACING + (ITEM_HEIGHT + 8) * 3;
        return height;
    }

    /**
     * Render section title
     */
    private void drawSection(GuiGraphics guiGraphics, int x, int y, String title, int width) {
        WidgetUtils.drawSectionTitle(guiGraphics, x, y, title);
        guiGraphics.fill(x, y + 14, x + width, y + 15, WidgetUtils.COLOR_BORDER);
    }

    /**
     * Render toggle setting item
     */
    private void drawToggleSetting(GuiGraphics guiGraphics, int x, int y, String label, boolean value, Runnable onClick) {
        // Draw label with vertical centering
        int labelY = y + (WidgetUtils.STANDARD_TOGGLE_HEIGHT - client.font.lineHeight) / 2;
        guiGraphics.drawString(client.font, label, x, labelY, WidgetUtils.COLOR_TEXT, false);

        int toggleX = x + WIDGET_OFFSET_X;
        WidgetUtils.drawToggle(guiGraphics, toggleX, y, value);
        // Register widget
        clickableWidgets.add(new ClickableWidget(toggleX, y, WidgetUtils.STANDARD_TOGGLE_WIDTH, WidgetUtils.STANDARD_TOGGLE_HEIGHT, WidgetType.TOGGLE, onClick, null));
    }

    /**
     * Render enum setting item
     */
    private void drawEnumSetting(GuiGraphics guiGraphics, int x, int y, String label, String value, Runnable onClick) {
        // Draw label with vertical centering
        int labelY = y + (20 - client.font.lineHeight) / 2;
        guiGraphics.drawString(client.font, label, x, labelY, WidgetUtils.COLOR_TEXT, false);

        int enumX = x + WIDGET_OFFSET_X;
        int enumWidth = 100;
        WidgetUtils.drawBorderedRect(guiGraphics, enumX, y, enumWidth, 20, WidgetUtils.COLOR_WIDGET_BG, WidgetUtils.COLOR_BORDER);

        int textY = y + (20 - client.font.lineHeight) / 2;
        guiGraphics.drawString(client.font, value, enumX + 8, textY, WidgetUtils.COLOR_TEXT_SECONDARY, false);
        // Register widget
        clickableWidgets.add(new ClickableWidget(enumX, y, enumWidth, 20, WidgetType.ENUM, onClick, null));
    }

    /**
     * Render color setting item
     */
    private void drawColorSetting(GuiGraphics guiGraphics, int x, int y, String label, int color) {
        int labelY = y + (20 - client.font.lineHeight) / 2;
        guiGraphics.drawString(client.font, label, x, labelY, WidgetUtils.COLOR_TEXT, false);
        WidgetUtils.drawColorBox(guiGraphics, x + WIDGET_OFFSET_X, y, 20, color);
        // Color picker will be implemented later (currently no click event)
    }

    /**
     * Render slider setting item
     */
    private void drawSliderSetting(GuiGraphics guiGraphics, int x, int y, String label, float value, SliderHandler handler) {
        int labelY = y + (WidgetUtils.STANDARD_SLIDER_HEIGHT - client.font.lineHeight) / 2;
        guiGraphics.drawString(client.font, label, x, labelY, WidgetUtils.COLOR_TEXT, false);

        int sliderX = x + WIDGET_OFFSET_X;
        int sliderY = y;
        WidgetUtils.drawSlider(guiGraphics, sliderX, sliderY, WidgetUtils.STANDARD_SLIDER_WIDTH, WidgetUtils.STANDARD_SLIDER_HEIGHT,
                value, WidgetUtils.COLOR_WIDGET_BG, WidgetUtils.COLOR_ACCENT);

        String percentText = String.format("%.0f%%", value * 100);
        guiGraphics.drawString(client.font, percentText, sliderX + WidgetUtils.STANDARD_SLIDER_WIDTH + 8, labelY, WidgetUtils.COLOR_TEXT_SECONDARY, false);
        // Register widget
        clickableWidgets.add(new ClickableWidget(sliderX, sliderY, WidgetUtils.STANDARD_SLIDER_WIDTH, WidgetUtils.STANDARD_SLIDER_HEIGHT, WidgetType.SLIDER, null, handler));
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button != 0) return false;

        // Check scrollbar click
        int totalContentHeight = getContentHeight();
        float visibleRatio = (float)(contentHeight - contentStartY) / totalContentHeight;
        if (visibleRatio < 1.0f) {
            int maxScroll = Math.max(0, totalContentHeight - (contentHeight - contentStartY));
            float scrollProgress = maxScroll > 0 ? (float)scrollOffset / maxScroll : 0;
            int[] thumbBounds = WidgetUtils.getScrollbarThumbBounds(scrollbarX, scrollbarY, scrollbarHeight, scrollProgress, visibleRatio);

            if (WidgetUtils.isMouseOver(mouseX, mouseY, thumbBounds[0], thumbBounds[1], thumbBounds[2], thumbBounds[3])) {
                isDraggingScrollbar = true;
                return true;
            }
        }

        for (ClickableWidget widget : clickableWidgets) {
            if (widget.contains(mouseX, mouseY)) {
                if (widget.type == WidgetType.SLIDER && widget.sliderHandler != null) {
                    // Start slider drag
                    draggingSlider = widget;
                    float value = (float) Math.max(0, Math.min(1, (mouseX - widget.x) / widget.width));
                    widget.sliderHandler.onDrag(value);
                    return true;
                } else if (widget.onClick != null) {
                    widget.onClick.run();
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (isDraggingScrollbar) {
            isDraggingScrollbar = false;
            return true;
        }
        if (draggingSlider != null) {
            draggingSlider = null;
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (isDraggingScrollbar) {
            int totalContentHeight = getContentHeight();
            int maxScroll = Math.max(0, totalContentHeight - (contentHeight - contentStartY));
            float visibleRatio = (float)(contentHeight - contentStartY) / totalContentHeight;
            int thumbHeight = Math.max(20, (int)(scrollbarHeight * visibleRatio));

            float progress = (float)(mouseY - scrollbarY - thumbHeight / 2) / (scrollbarHeight - thumbHeight);
            progress = Math.max(0, Math.min(1, progress));
            scrollOffset = (int)(progress * maxScroll);
            return true;
        }
        if (draggingSlider != null && draggingSlider.sliderHandler != null) {
            float value = (float) Math.max(0, Math.min(1, (mouseX - draggingSlider.x) / draggingSlider.width));
            draggingSlider.sliderHandler.onDrag(value);
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        int maxScroll = getContentHeight() - (contentHeight - contentStartY);
        maxScroll = Math.max(0, maxScroll);
        this.scrollOffset = (int) Math.max(0, Math.min(maxScroll, this.scrollOffset - scrollY * SCROLL_STEP));
        return true;
    }

    @Override
    public void save() {
        // Settings are saved in real-time or by parent
    }
}
