package com.hyfata.najoan.koreanpatch.config.screen.tab;

import com.hyfata.najoan.koreanpatch.config.ConfigManager;
import com.hyfata.najoan.koreanpatch.config.category.CategoryInput;
import com.hyfata.najoan.koreanpatch.config.category.input.AutoLangTypeMode;
import com.hyfata.najoan.koreanpatch.config.screen.widget.WidgetUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Input settings tab
 * Manage text input behavior and IME-related settings
 */
public class InputSettingsTab extends SettingsTab {
    private CategoryInput config;
    private int scrollOffset = 0;
    private static final int SCROLL_STEP = 15;
    private static final int SECTION_SPACING = 25;
    private static final int SECTION_GAP = 15;
    private static final int ITEM_HEIGHT = 22;
    private static final int CONTENT_WIDTH = 350;
    private static final int WIDGET_OFFSET_X = 200;

    private final List<ClickableWidget> clickableWidgets = new ArrayList<>();

    private boolean isDraggingScrollbar = false;
    private int scrollbarX, scrollbarY, scrollbarHeight;

    private static final Minecraft client = Minecraft.getInstance();

    private record ClickableWidget(int x, int y, int width, int height, Runnable onClick) {
        boolean contains(double mx, double my) {
            return mx >= x && mx < x + width && my >= y && my < y + height;
        }
    }

    @Override
    public Component getTabName() {
        return Component.translatable("koreanpatch.config.tab.input");
    }

    @Override
    protected void onInit() {
        this.config = ConfigManager.getInstance().getConfig().getCategoryInput();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        clickableWidgets.clear();

        int padding = 15;
        int contentX = (contentWidth - CONTENT_WIDTH) / 2;
        int y = contentStartY + padding - scrollOffset;

        // General settings section
        drawSection(guiGraphics, contentX, y, Component.translatable("koreanpatch.config.input.general").getString(), CONTENT_WIDTH);
        y += SECTION_SPACING;

        drawEnumSetting(guiGraphics, contentX, y,
                Component.translatable("koreanpatch.config.input.general.auto_lang_type_mode").getString(),
                getAutoLangTypeModeName(config.getAutoLangTypeMode()),
                () -> {
                    AutoLangTypeMode[] values = AutoLangTypeMode.values();
                    int next = (config.getAutoLangTypeMode().ordinal() + 1) % values.length;
                    config.setAutoLangTypeMode(values[next]);
                });
        y += ITEM_HEIGHT + 8;

        drawToggleSetting(guiGraphics, contentX, y,
                Component.translatable("koreanpatch.config.input.general.memory_lang_type").getString(),
                config.isMemoryLangTypePerScreen(),
                () -> config.setMemoryLangTypePerScreen(!config.isMemoryLangTypePerScreen()));
        y += ITEM_HEIGHT + 8;

        y += SECTION_GAP;

        // IME settings section
        drawSection(guiGraphics, contentX, y, Component.translatable("koreanpatch.config.input.ime").getString(), CONTENT_WIDTH);
        y += SECTION_SPACING;

        drawToggleSetting(guiGraphics, contentX, y,
                Component.translatable("koreanpatch.config.input.ime.disable_ime_playing").getString(),
                config.isDisableImeWhenPlaying(),
                () -> config.setDisableImeWhenPlaying(!config.isDisableImeWhenPlaying()));
        y += ITEM_HEIGHT + 8;

        drawToggleSetting(guiGraphics, contentX, y,
                Component.translatable("koreanpatch.config.input.ime.auto_ime_switch").getString(),
                config.isAutoImeSwitch(),
                () -> config.setAutoImeSwitch(!config.isAutoImeSwitch()));
        y += ITEM_HEIGHT + 8;

        drawToggleSetting(guiGraphics, contentX, y,
                Component.translatable("koreanpatch.config.input.ime.always_ime_enabled").getString(),
                config.isAlwaysImeEnabled(),
                () -> config.setAlwaysImeEnabled(!config.isAlwaysImeEnabled()));
        y += ITEM_HEIGHT + 8;

        y += SECTION_GAP;

        // Information section
        drawSection(guiGraphics, contentX, y, "Information", CONTENT_WIDTH);
        y += SECTION_SPACING;

        guiGraphics.drawString(
                client.font,
                Component.translatable("koreanpatch.config.input.info").getString(),
                contentX, y, WidgetUtils.COLOR_TEXT_SECONDARY, false
        );

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

    private String getAutoLangTypeModeName(AutoLangTypeMode mode) {
        return switch (mode) {
            case AUTO -> Component.translatable("koreanpatch.config.input.mode.auto").getString();
            case KOREAN -> Component.translatable("koreanpatch.config.input.mode.korean").getString();
            case ENGLISH -> Component.translatable("koreanpatch.config.input.mode.english").getString();
            case IME -> Component.translatable("koreanpatch.config.input.mode.ime").getString();
        };
    }

    private int getContentHeight() {
        int height = 15;
        height += SECTION_SPACING + (ITEM_HEIGHT + 8) * 2 + SECTION_GAP;
        height += SECTION_SPACING + (ITEM_HEIGHT + 8) * 3 + SECTION_GAP;
        height += SECTION_SPACING + ITEM_HEIGHT;
        return height;
    }

    private void drawSection(GuiGraphics guiGraphics, int x, int y, String title, int width) {
        WidgetUtils.drawSectionTitle(guiGraphics, x, y, title);
        guiGraphics.fill(x, y + 14, x + width, y + 15, WidgetUtils.COLOR_BORDER);
    }

    private void drawToggleSetting(GuiGraphics guiGraphics, int x, int y, String label, boolean value, Runnable onClick) {
        int labelY = y + (WidgetUtils.STANDARD_TOGGLE_HEIGHT - client.font.lineHeight) / 2;
        guiGraphics.drawString(client.font, label, x, labelY, WidgetUtils.COLOR_TEXT, false);

        int toggleX = x + WIDGET_OFFSET_X;
        WidgetUtils.drawToggle(guiGraphics, toggleX, y, value);
        clickableWidgets.add(new ClickableWidget(toggleX, y, WidgetUtils.STANDARD_TOGGLE_WIDTH, WidgetUtils.STANDARD_TOGGLE_HEIGHT, onClick));
    }

    private void drawEnumSetting(GuiGraphics guiGraphics, int x, int y, String label, String value, Runnable onClick) {
        int labelY = y + (20 - client.font.lineHeight) / 2;
        guiGraphics.drawString(client.font, label, x, labelY, WidgetUtils.COLOR_TEXT, false);

        int enumX = x + WIDGET_OFFSET_X;
        int enumWidth = 120;
        WidgetUtils.drawBorderedRect(guiGraphics, enumX, y, enumWidth, 20, WidgetUtils.COLOR_WIDGET_BG, WidgetUtils.COLOR_BORDER);

        int textY = y + (20 - client.font.lineHeight) / 2;
        guiGraphics.drawString(client.font, value, enumX + 8, textY, WidgetUtils.COLOR_TEXT_SECONDARY, false);
        clickableWidgets.add(new ClickableWidget(enumX, y, enumWidth, 20, onClick));
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button != 0) return false;

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
            if (widget.contains(mouseX, mouseY) && widget.onClick != null) {
                widget.onClick.run();
                return true;
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
        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        int maxScroll = getContentHeight() - (contentHeight - contentStartY);
        maxScroll = Math.max(0, maxScroll);
        this.scrollOffset = (int) Math.max(0, Math.min(maxScroll, this.scrollOffset - scrollY * SCROLL_STEP));
        return true;
    }
}
