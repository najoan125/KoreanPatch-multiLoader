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
    private static final int SECTION_SPACING = 30;
    private static final int ITEM_HEIGHT = 25;
    private static final int TOGGLE_WIDTH = 40;
    private static final int TOGGLE_HEIGHT = 20;

    // Track clickable widgets
    private final List<ClickableWidget> clickableWidgets = new ArrayList<>();

    private static final Minecraft client = Minecraft.getInstance();

    // Clickable widget information
    private record ClickableWidget(int x, int y, int width, int height, Runnable onClick) {
        boolean contains(double mx, double my) {
            return mx >= x && mx < x + width && my >= y && my < y + height;
        }
    }

    @Override
    public Component getTabName() {
        return Component.literal("Input");
    }

    @Override
    protected void onInit() {
        this.config = ConfigManager.getInstance().getConfig().getCategoryInput();
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

        // Auto language mode
        drawEnumSetting(guiGraphics, padding, y, "Auto Language Mode",
                config.getAutoLangTypeMode().name(),
                () -> {
                    AutoLangTypeMode[] values = AutoLangTypeMode.values();
                    int next = (config.getAutoLangTypeMode().ordinal() + 1) % values.length;
                    config.setAutoLangTypeMode(values[next]);
                });
        y += ITEM_HEIGHT + 15;

        // Remember language state per screen
        drawToggleSetting(guiGraphics, padding, y, "Remember Language State Per Screen",
                config.isMemoryLangTypePerScreen(),
                () -> config.setMemoryLangTypePerScreen(!config.isMemoryLangTypePerScreen()));
        y += ITEM_HEIGHT + 10;

        // IME settings section
        drawSection(guiGraphics, padding, y, "IME Settings", maxWidth);
        y += SECTION_SPACING;

        // Disable IME while playing
        drawToggleSetting(guiGraphics, padding, y, "Disable IME While Playing",
                config.isDisableImeWhenPlaying(),
                () -> config.setDisableImeWhenPlaying(!config.isDisableImeWhenPlaying()));
        y += ITEM_HEIGHT + 10;

        // Auto IME switch
        drawToggleSetting(guiGraphics, padding, y, "Auto IME Switch",
                config.isAutoImeSwitch(),
                () -> config.setAutoImeSwitch(!config.isAutoImeSwitch()));
        y += ITEM_HEIGHT + 10;

        // Always enable IME
        drawToggleSetting(guiGraphics, padding, y, "Always Enable IME",
                config.isAlwaysImeEnabled(),
                () -> config.setAlwaysImeEnabled(!config.isAlwaysImeEnabled()));
        y += ITEM_HEIGHT + 10;

        // Information section
        drawSection(guiGraphics, padding, y, "Information", maxWidth);
        y += SECTION_SPACING;

        guiGraphics.drawString(
                client.font,
                "Auto language mode can be changed in the Key Bindings settings tab.",
                padding, y, WidgetUtils.COLOR_TEXT_SECONDARY, false
        );

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
        int padding = 15;
        int height = padding;
        // General settings: section + 1 enum + 1 toggle
        height += SECTION_SPACING + (ITEM_HEIGHT + 15) + (ITEM_HEIGHT + 10);
        // IME settings: section + 3 toggles
        height += SECTION_SPACING + (ITEM_HEIGHT + 10) * 3;
        // Information: section + text
        height += SECTION_SPACING + ITEM_HEIGHT;
        return height;
    }

    /**
     * Render section title
     */
    private void drawSection(GuiGraphics guiGraphics, int x, int y, String title, int width) {
        guiGraphics.drawString(client.font, title, x, y, WidgetUtils.COLOR_TEXT, false);
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
        clickableWidgets.add(new ClickableWidget(toggleX, y, TOGGLE_WIDTH, TOGGLE_HEIGHT, onClick));
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
        guiGraphics.drawString(client.font, value, enumX + 5, y,
                WidgetUtils.COLOR_TEXT_SECONDARY, false);
        // Register widget
        clickableWidgets.add(new ClickableWidget(enumX, y - 2, enumWidth, 20, onClick));
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button != 0) return false;

        for (ClickableWidget widget : clickableWidgets) {
            if (widget.contains(mouseX, mouseY) && widget.onClick != null) {
                widget.onClick.run();
                return true;
            }
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
