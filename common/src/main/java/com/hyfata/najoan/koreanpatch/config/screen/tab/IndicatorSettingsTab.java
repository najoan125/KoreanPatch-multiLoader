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
    private static final int SECTION_SPACING = 30;
    private static final int ITEM_HEIGHT = 25;
    private static final int TOGGLE_WIDTH = 40;
    private static final int TOGGLE_HEIGHT = 20;
    private static final int SLIDER_WIDTH = 80;
    private static final int SLIDER_HEIGHT = 15;

    // Track clickable widgets
    private final List<ClickableWidget> clickableWidgets = new ArrayList<>();
    private ClickableWidget draggingSlider = null;

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
        return Component.literal("Indicator");
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
        int y = contentStartY + padding - scrollOffset;
        int maxWidth = contentWidth - padding * 2;

        // General settings section
        drawSection(guiGraphics, padding, y, "General Settings", maxWidth);
        y += SECTION_SPACING;

        // Show indicator toggle
        drawToggleSetting(guiGraphics, padding, y, "Show Indicator",
                config.isShowIndicator(),
                () -> config.setShowIndicator(!config.isShowIndicator()));
        y += ITEM_HEIGHT + 10;

        // Outline settings section
        drawSection(guiGraphics, padding, y, "Outline Settings", maxWidth);
        y += SECTION_SPACING;

        drawToggleSetting(guiGraphics, padding, y, "Show Outline",
                config.getOutlineSettings().isShowOutline(),
                () -> config.getOutlineSettings().setShowOutline(!config.getOutlineSettings().isShowOutline()));
        y += ITEM_HEIGHT + 10;

        drawEnumSetting(guiGraphics, padding, y, "Outline Type",
                config.getOutlineSettings().getOutlineType().name(),
                () -> {
                    OutlineType[] values = OutlineType.values();
                    int next = (config.getOutlineSettings().getOutlineType().ordinal() + 1) % values.length;
                    config.getOutlineSettings().setOutlineType(values[next]);
                });
        y += ITEM_HEIGHT + 10;

        drawColorSetting(guiGraphics, padding, y, "Korean Color",
                config.getOutlineSettings().getColorOpacitySettings().getKoreanColor().getRGB());
        y += ITEM_HEIGHT + 10;

        drawColorSetting(guiGraphics, padding, y, "English Color",
                config.getOutlineSettings().getColorOpacitySettings().getEnColor().getRGB());
        y += ITEM_HEIGHT + 10;

        drawSliderSetting(guiGraphics, padding, y, "Outline Opacity",
                config.getOutlineSettings().getColorOpacitySettings().getOpacity() / 100f,
                v -> config.getOutlineSettings().getColorOpacitySettings().setOpacity((int) (v * 100)));
        y += ITEM_HEIGHT + 10;

        // Background settings section
        drawSection(guiGraphics, padding, y, "Background Settings", maxWidth);
        y += SECTION_SPACING;

        drawColorSetting(guiGraphics, padding, y, "Korean Background Color",
                config.getBackgroundSettings().getKoreanColor().getRGB());
        y += ITEM_HEIGHT + 10;

        drawColorSetting(guiGraphics, padding, y, "English Background Color",
                config.getBackgroundSettings().getEnColor().getRGB());
        y += ITEM_HEIGHT + 10;

        drawSliderSetting(guiGraphics, padding, y, "Background Opacity",
                config.getBackgroundSettings().getOpacity() / 100f,
                v -> config.getBackgroundSettings().setOpacity((int) (v * 100)));
        y += ITEM_HEIGHT + 10;

        // Text settings section
        drawSection(guiGraphics, padding, y, "Text Settings", maxWidth);
        y += SECTION_SPACING;

        drawColorSetting(guiGraphics, padding, y, "Korean Text",
                config.getTextSettings().getKoreanColor().getRGB());
        y += ITEM_HEIGHT + 10;

        drawColorSetting(guiGraphics, padding, y, "English Text",
                config.getTextSettings().getEnColor().getRGB());
        y += ITEM_HEIGHT + 10;

        drawSliderSetting(guiGraphics, padding, y, "Text Opacity",
                config.getTextSettings().getOpacity() / 100f,
                v -> config.getTextSettings().setOpacity((int) (v * 100)));
        y += ITEM_HEIGHT + 10;

        // Animation settings section
        drawSection(guiGraphics, padding, y, "Animation Settings", maxWidth);
        y += SECTION_SPACING;

        drawToggleSetting(guiGraphics, padding, y, "Animation",
                config.getAnimationSettings().isShowAnimation(),
                () -> config.getAnimationSettings().setShowAnimation(!config.getAnimationSettings().isShowAnimation()));
        y += ITEM_HEIGHT + 10;

        drawEnumSetting(guiGraphics, padding, y, "Easing Function",
                config.getAnimationSettings().getEasingFunction().name(),
                () -> {
                    EasingFunctions[] values = EasingFunctions.values();
                    int next = (config.getAnimationSettings().getEasingFunction().ordinal() + 1) % values.length;
                    config.getAnimationSettings().setEasingFunction(values[next]);
                });
        y += ITEM_HEIGHT + 10;

        drawSliderSetting(guiGraphics, padding, y, "Animation Speed",
                config.getAnimationSettings().getSpeed() / 100f,
                v -> config.getAnimationSettings().setSpeed((int) (v * 100)));

        // Draw scrollbar
        int scrollbarX = contentWidth - 10;
        int scrollbarY = contentStartY;
        int scrollbarHeight = contentHeight - contentStartY;
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
        // General settings: section + 1 toggle
        height += SECTION_SPACING + (ITEM_HEIGHT + 10);
        // Outline settings: section + 1 toggle + 1 enum + 2 colors + 1 slider
        height += SECTION_SPACING + (ITEM_HEIGHT + 10) * 5;
        // Background settings: section + 2 colors + 1 slider
        height += SECTION_SPACING + (ITEM_HEIGHT + 10) * 3;
        // Text settings: section + 2 colors + 1 slider
        height += SECTION_SPACING + (ITEM_HEIGHT + 10) * 3;
        // Animation settings: section + 1 toggle + 1 enum + 1 slider
        height += SECTION_SPACING + (ITEM_HEIGHT + 10) * 3;
        return height;
    }

    /**
     * Render section title
     */
    private void drawSection(GuiGraphics guiGraphics, int x, int y, String title, int width) {
        WidgetUtils.drawSectionTitle(guiGraphics, x, y, title);
        guiGraphics.fill(x, y + 15, x + width, y + 16, WidgetUtils.COLOR_BORDER);
    }

    /**
     * Render toggle setting item
     */
    private void drawToggleSetting(GuiGraphics guiGraphics, int x, int y, String label, boolean value, Runnable onClick) {
        guiGraphics.drawString(client.font, label, x, y, WidgetUtils.COLOR_TEXT, false);
        int toggleX = x + 250;
        WidgetUtils.drawToggle(guiGraphics, toggleX, y, value);
        // Register widget
        clickableWidgets.add(new ClickableWidget(toggleX, y, TOGGLE_WIDTH, TOGGLE_HEIGHT, WidgetType.TOGGLE, onClick, null));
    }

    /**
     * Render enum setting item
     */
    private void drawEnumSetting(GuiGraphics guiGraphics, int x, int y, String label, String value, Runnable onClick) {
        guiGraphics.drawString(client.font, label, x, y, WidgetUtils.COLOR_TEXT, false);
        int enumX = x + 250;
        int enumWidth = 100;
        WidgetUtils.drawBorderedRect(guiGraphics, enumX, y - 2, enumWidth, 20,
                WidgetUtils.COLOR_WIDGET_BG, WidgetUtils.COLOR_BORDER);
        guiGraphics.drawString(client.font, value, enumX + 5, y, WidgetUtils.COLOR_TEXT_SECONDARY, false);
        // Register widget
        clickableWidgets.add(new ClickableWidget(enumX, y - 2, enumWidth, 20, WidgetType.ENUM, onClick, null));
    }

    /**
     * Render color setting item
     */
    private void drawColorSetting(GuiGraphics guiGraphics, int x, int y, String label, int color) {
        guiGraphics.drawString(client.font, label, x, y, WidgetUtils.COLOR_TEXT, false);
        WidgetUtils.drawColorBox(guiGraphics, x + 250, y, 20, color);
        // Color picker will be implemented later (currently no click event)
    }

    /**
     * Render slider setting item
     */
    private void drawSliderSetting(GuiGraphics guiGraphics, int x, int y, String label, float value, SliderHandler handler) {
        guiGraphics.drawString(client.font, label, x, y, WidgetUtils.COLOR_TEXT, false);
        int sliderX = x + 250;
        int sliderY = y + 3;
        WidgetUtils.drawSlider(guiGraphics, sliderX, sliderY, SLIDER_WIDTH, SLIDER_HEIGHT,
                value, WidgetUtils.COLOR_WIDGET_BG, WidgetUtils.COLOR_ACCENT);
        String percentText = String.format("%.0f%%", value * 100);
        guiGraphics.drawString(client.font, percentText, sliderX + SLIDER_WIDTH + 5, y, WidgetUtils.COLOR_TEXT_SECONDARY, false);
        // Register widget
        clickableWidgets.add(new ClickableWidget(sliderX, sliderY, SLIDER_WIDTH, SLIDER_HEIGHT, WidgetType.SLIDER, null, handler));
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button != 0) return false;

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
        if (draggingSlider != null) {
            draggingSlider = null;
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
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
