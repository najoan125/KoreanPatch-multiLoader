package com.hyfata.najoan.koreanpatch.config.screen;

import com.hyfata.najoan.koreanpatch.config.ConfigManager;
import com.hyfata.najoan.koreanpatch.config.screen.tab.SettingsTab;
import com.hyfata.najoan.koreanpatch.config.screen.tab.IndicatorSettingsTab;
import com.hyfata.najoan.koreanpatch.config.screen.tab.InputSettingsTab;
import com.hyfata.najoan.koreanpatch.config.screen.tab.KeyBindingsSettingsTab;
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
    private static final int TAB_WIDTH = 100;
    private static final int TAB_HEIGHT = 30;
    private static final int TAB_SPACING = 10;
    private static final int TAB_AREA_HEIGHT = TAB_HEIGHT + 20; // Tab and separator line
    private static final int PADDING = 15;
    private static final int CONTENT_TOP = TAB_AREA_HEIGHT + PADDING;

    private final Screen previousScreen;
    private final List<SettingsTab> tabs = new ArrayList<>();
    private int currentTabIndex = 0;
    private int tabStartX;

    // Color settings
    private static final int COLOR_BACKGROUND = 0xFF1A1A1A;
    private static final int COLOR_TAB_INACTIVE = 0xFF2D2D2D;
    private static final int COLOR_TAB_ACTIVE = 0xFF3D3D3D;
    private static final int COLOR_TAB_ACTIVE_BORDER = 0xFF4CAF50;
    private static final int COLOR_TEXT = 0xFFEEEEEE;
    private static final int COLOR_TEXT_SECONDARY = 0xFF999999;
    private static final int COLOR_BORDER = 0xFF404040;

    public ModernSettingsScreen(Screen previousScreen) {
        super(Component.literal("Settings"));
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

        // Initialize all tabs
        for (SettingsTab tab : tabs) {
            tab.init(this, CONTENT_TOP, this.width, this.height);
        }
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        // Render background (directly implemented due to blur constraints in renderBackground)
        guiGraphics.fill(0, 0, this.width, this.height, COLOR_BACKGROUND);

        // Render title
        guiGraphics.drawCenteredString(
                this.font,
                this.title,
                this.width / 2,
                10,
                COLOR_TEXT
        );

        // Render tabs
        renderTabs(guiGraphics, mouseX, mouseY);

        // Tab separator line
        guiGraphics.fill(0, TAB_AREA_HEIGHT - 1, this.width, TAB_AREA_HEIGHT, COLOR_BORDER);

        // Render current tab content with scissor clipping to prevent overlap with tab bar
        guiGraphics.enableScissor(0, TAB_AREA_HEIGHT, this.width, this.height);
        getCurrentTab().render(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.disableScissor();
    }

    /**
     * Render tab UI
     */
    private void renderTabs(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        for (int i = 0; i < tabs.size(); i++) {
            SettingsTab tab = tabs.get(i);
            int x = tabStartX + i * (TAB_WIDTH + TAB_SPACING);
            int y = 5;

            boolean isActive = i == currentTabIndex;
            boolean isHovered = mouseX >= x && mouseX < x + TAB_WIDTH &&
                    mouseY >= y && mouseY < y + TAB_HEIGHT;

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

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        return getCurrentTab().mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    @Override
    public boolean mouseClicked(@NotNull MouseButtonEvent event, boolean isDoubleClick) {
        double mouseX = event.x();
        double mouseY = event.y();
        int button = event.button();

        // Handle tab click
        for (int i = 0; i < tabs.size(); i++) {
            int x = tabStartX + i * (TAB_WIDTH + TAB_SPACING);
            int y = 5;

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

        if (keyCode == 256) { // ESC
            this.onClose();
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

    @Override
    public void onClose() {
        // Save changes from all tabs
        for (SettingsTab tab : tabs) {
            tab.save();
        }
        ConfigManager.getInstance().saveConfig(ConfigManager.getInstance().getConfig());
        this.minecraft.setScreen(previousScreen);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }
}