package com.hyfata.najoan.koreanpatch.config.screen;

import com.hyfata.najoan.koreanpatch.config.ConfigManager;
import com.hyfata.najoan.koreanpatch.config.screen.tab.SettingsTab;
import com.hyfata.najoan.koreanpatch.config.screen.tab.IndicatorSettingsTab;
import com.hyfata.najoan.koreanpatch.config.screen.tab.InputSettingsTab;
import com.hyfata.najoan.koreanpatch.config.screen.tab.KeyBindingsSettingsTab;
import com.hyfata.najoan.koreanpatch.config.screen.widget.WidgetUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom modern settings screen
 * Manages indicator, input, and keybinding settings with a tab-based UI.
 */
public class ModernSettingsScreen extends Screen {
    private static final int TAB_WIDTH = 90;
    private static final int TAB_HEIGHT = 24;
    private static final int TAB_SPACING = 8;
    private static final int TAB_AREA_HEIGHT = TAB_HEIGHT + 35;
    private static final int BOTTOM_BAR_HEIGHT = 40;
    private static final int BUTTON_WIDTH = 100;
    private static final int BUTTON_HEIGHT = 20;
    private static final int BUTTON_SPACING = 10;
    private static final int PADDING = 12;

    private final Screen previousScreen;
    private final List<SettingsTab> tabs = new ArrayList<>();
    private int currentTabIndex = 0;
    private int tabStartX;

    // Bottom bar button bounds
    private int cancelButtonX, cancelButtonY;
    private int doneButtonX, doneButtonY;

    // Hover state tracking
    private boolean isOverClickable = false;

    // Color settings
    private static final int COLOR_BACKGROUND = 0xFF1A1A1A;
    private static final int COLOR_TAB_INACTIVE = 0xFF2D2D2D;
    private static final int COLOR_TAB_ACTIVE = 0xFF3D3D3D;
    private static final int COLOR_TAB_ACTIVE_BORDER = 0xFF4CAF50;
    private static final int COLOR_TEXT = 0xFFEEEEEE;
    private static final int COLOR_TEXT_SECONDARY = 0xFF999999;
    private static final int COLOR_BORDER = 0xFF404040;
    private static final int COLOR_BOTTOM_BAR = 0xFF252525;

    public ModernSettingsScreen(Screen previousScreen) {
        super(Component.translatable("koreanpatch.config.title"));
        this.previousScreen = previousScreen;
        this.tabs.add(new IndicatorSettingsTab());
        this.tabs.add(new InputSettingsTab());
        this.tabs.add(new KeyBindingsSettingsTab());
    }

    @Override
    protected void init() {
        super.init();

        // Tab start X position (center alignment)
        int totalTabWidth = (TAB_WIDTH + TAB_SPACING) * tabs.size() - TAB_SPACING;
        this.tabStartX = (this.width - totalTabWidth) / 2;

        // Calculate content area (between tab bar and bottom bar)
        int contentTop = TAB_AREA_HEIGHT + PADDING;
        int contentBottom = this.height - BOTTOM_BAR_HEIGHT;

        // Initialize all tabs with adjusted content area
        for (SettingsTab tab : tabs) {
            tab.init(this, contentTop, this.width, contentBottom);
        }

        // Calculate bottom bar button positions
        int buttonTotalWidth = BUTTON_WIDTH * 2 + BUTTON_SPACING;
        cancelButtonX = (this.width - buttonTotalWidth) / 2;
        doneButtonX = cancelButtonX + BUTTON_WIDTH + BUTTON_SPACING;
        cancelButtonY = this.height - BOTTOM_BAR_HEIGHT + (BOTTOM_BAR_HEIGHT - BUTTON_HEIGHT) / 2;
        doneButtonY = cancelButtonY;
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        // Reset cursor state
        isOverClickable = false;

        // Render background
        guiGraphics.fill(0, 0, this.width, this.height, COLOR_BACKGROUND);

        // Render title
        guiGraphics.drawCenteredString(
                this.font,
                this.title,
                this.width / 2,
                8,
                COLOR_TEXT
        );

        // Render tabs
        renderTabs(guiGraphics, mouseX, mouseY);

        // Tab separator line
        guiGraphics.fill(0, TAB_AREA_HEIGHT - 1, this.width, TAB_AREA_HEIGHT, COLOR_BORDER);

        // Render current tab content with scissor clipping
        guiGraphics.enableScissor(0, TAB_AREA_HEIGHT, this.width, this.height - BOTTOM_BAR_HEIGHT);
        getCurrentTab().render(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.disableScissor();

        // Render bottom bar
        renderBottomBar(guiGraphics, mouseX, mouseY);

        // Update cursor based on hover state
        if (isOverClickable) {
            WidgetUtils.setHandCursor();
        } else {
            WidgetUtils.setDefaultCursor();
        }
    }

    /**
     * Render tab UI
     */
    private void renderTabs(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        for (int i = 0; i < tabs.size(); i++) {
            SettingsTab tab = tabs.get(i);
            int x = tabStartX + i * (TAB_WIDTH + TAB_SPACING);
            int y = 28;

            boolean isActive = i == currentTabIndex;
            boolean isHovered = mouseX >= x && mouseX < x + TAB_WIDTH &&
                    mouseY >= y && mouseY < y + TAB_HEIGHT;

            if (isHovered) {
                isOverClickable = true;
            }

            // Tab background
            int bgColor = isActive ? COLOR_TAB_ACTIVE : COLOR_TAB_INACTIVE;
            guiGraphics.fill(x, y, x + TAB_WIDTH, y + TAB_HEIGHT, bgColor);

            // Active tab top border
            if (isActive) {
                guiGraphics.fill(x, y, x + TAB_WIDTH, y + 2, COLOR_TAB_ACTIVE_BORDER);
            }

            // Tab text
            int textColor = isActive ? 0xFFFFFFFF : COLOR_TEXT_SECONDARY;
            guiGraphics.drawCenteredString(
                    this.font,
                    tab.getTabName(),
                    x + TAB_WIDTH / 2,
                    y + (TAB_HEIGHT - this.font.lineHeight) / 2,
                    textColor
            );

            // Hover effect
            if (isHovered && !isActive) {
                guiGraphics.fill(x, y, x + TAB_WIDTH, y + TAB_HEIGHT, 0x33FFFFFF);
            }
        }
    }

    /**
     * Render bottom bar with Cancel and Done buttons
     */
    private void renderBottomBar(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        // Bottom bar background
        guiGraphics.fill(0, this.height - BOTTOM_BAR_HEIGHT, this.width, this.height, COLOR_BOTTOM_BAR);
        // Top border
        guiGraphics.fill(0, this.height - BOTTOM_BAR_HEIGHT, this.width, this.height - BOTTOM_BAR_HEIGHT + 1, COLOR_BORDER);

        // Cancel button
        boolean cancelHovered = WidgetUtils.isMouseOver(mouseX, mouseY, cancelButtonX, cancelButtonY, BUTTON_WIDTH, BUTTON_HEIGHT);
        if (cancelHovered) isOverClickable = true;
        drawButton(guiGraphics, cancelButtonX, cancelButtonY, BUTTON_WIDTH, BUTTON_HEIGHT,
                Component.translatable("koreanpatch.config.cancel").getString(), cancelHovered);

        // Done button
        boolean doneHovered = WidgetUtils.isMouseOver(mouseX, mouseY, doneButtonX, doneButtonY, BUTTON_WIDTH, BUTTON_HEIGHT);
        if (doneHovered) isOverClickable = true;
        drawButton(guiGraphics, doneButtonX, doneButtonY, BUTTON_WIDTH, BUTTON_HEIGHT,
                Component.translatable("koreanpatch.config.done").getString(), doneHovered);
    }

    /**
     * Draw a button with sharp corners
     */
    private void drawButton(GuiGraphics guiGraphics, int x, int y, int width, int height, String text, boolean hovered) {
        int bgColor = hovered ? WidgetUtils.COLOR_BUTTON_HOVER : WidgetUtils.COLOR_WIDGET_BG;
        WidgetUtils.drawBorderedRect(guiGraphics, x, y, width, height, bgColor, COLOR_BORDER);
        guiGraphics.drawCenteredString(this.font, text, x + width / 2, y + (height - this.font.lineHeight) / 2, COLOR_TEXT);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        return getCurrentTab().mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    @Override
    public boolean mouseClicked(@NotNull MouseButtonEvent event, boolean isDoubleClick) {
        double mouseX = event.x();
        double mouseY = event.y();
        int button = event.button();

        // Handle bottom bar button clicks
        if (button == 0) {
            // Cancel button
            if (WidgetUtils.isMouseOver(mouseX, mouseY, cancelButtonX, cancelButtonY, BUTTON_WIDTH, BUTTON_HEIGHT)) {
                cancelAndClose();
                return true;
            }

            // Done button
            if (WidgetUtils.isMouseOver(mouseX, mouseY, doneButtonX, doneButtonY, BUTTON_WIDTH, BUTTON_HEIGHT)) {
                saveAndClose();
                return true;
            }
        }

        // Handle tab click
        for (int i = 0; i < tabs.size(); i++) {
            int x = tabStartX + i * (TAB_WIDTH + TAB_SPACING);
            int y = 28;

            if (mouseX >= x && mouseX < x + TAB_WIDTH &&
                    mouseY >= y && mouseY < y + TAB_HEIGHT) {
                currentTabIndex = i;
                return true;
            }
        }

        // Handle mouse click in the current tab
        return getCurrentTab().mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent event) {
        double mouseX = event.x();
        double mouseY = event.y();
        int button = event.button();

        return getCurrentTab().mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent event, double dragX, double dragY) {
        double mouseX = event.x();
        double mouseY = event.y();
        int button = event.button();

        return getCurrentTab().mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        int keyCode = event.key();
        int scanCode = event.scancode();
        int modifiers = event.modifiers();

        // Check if current tab is recording keys
        if (getCurrentTab().isRecordingKey()) {
            // Let the tab handle the key press (including ESC for cancel)
            return getCurrentTab().keyPressed(keyCode, scanCode, modifiers);
        }

        if (keyCode == 256) { // ESC - close without saving
            cancelAndClose();
            return true;
        }

        return getCurrentTab().keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(KeyEvent event) {
        int keyCode = event.key();
        int scanCode = event.scancode();
        int modifiers = event.modifiers();

        return getCurrentTab().keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(CharacterEvent event) {
        char codePoint = (char) event.codepoint();
        int modifiers = event.modifiers();

        return getCurrentTab().charTyped(codePoint, modifiers);
    }

    /**
     * Get the current active tab
     */
    private SettingsTab getCurrentTab() {
        return tabs.get(currentTabIndex);
    }

    /**
     * Save all changes and close the screen
     */
    private void saveAndClose() {
        for (SettingsTab tab : tabs) {
            tab.save();
        }
        ConfigManager.getInstance().saveConfig(ConfigManager.getInstance().getConfig());
        closeScreen();
    }

    /**
     * Close without saving changes
     */
    private void cancelAndClose() {
        // Reload config to discard changes
        ConfigManager.getInstance().reloadConfig();
        closeScreen();
    }

    /**
     * Close the screen and reset cursor
     */
    private void closeScreen() {
        WidgetUtils.setDefaultCursor();
        this.minecraft.setScreen(previousScreen);
    }

    @Override
    public void onClose() {
        // Default close behavior - close without saving (same as ESC)
        cancelAndClose();
    }

    @Override
    public boolean shouldCloseOnEsc() {
        // Don't use built-in ESC handling, we handle it in keyPressed
        return false;
    }
}
