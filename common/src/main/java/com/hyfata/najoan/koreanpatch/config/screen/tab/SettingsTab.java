package com.hyfata.najoan.koreanpatch.config.screen.tab;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

/**
 * Base interface for settings screen tabs
 */
public abstract class SettingsTab {
    protected Screen screen;
    protected int contentStartY;
    protected int contentWidth;
    protected int contentHeight;

    /**
     * Tab name (text displayed in UI)
     */
    public abstract Component getTabName();

    /**
     * Initialize tab
     */
    public void init(Screen screen, int contentStartY, int contentWidth, int contentHeight) {
        this.screen = screen;
        this.contentStartY = contentStartY;
        this.contentWidth = contentWidth;
        this.contentHeight = contentHeight;
        onInit();
    }

    /**
     * Method called on tab initialization (implemented by subclasses)
     */
    protected abstract void onInit();

    /**
     * Render
     */
    public abstract void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick);

    /**
     * Mouse scroll
     */
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        return false;
    }

    /**
     * Mouse click
     */
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return false;
    }

    /**
     * Mouse release
     */
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return false;
    }

    /**
     * Mouse drag
     */
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        return false;
    }

    /**
     * Key pressed
     */
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return false;
    }

    /**
     * Key released
     */
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        return false;
    }

    /**
     * Character typed
     */
    public boolean charTyped(char codePoint, int modifiers) {
        return false;
    }

    /**
     * Check if this tab is currently recording key bindings
     * Used to prevent ESC from closing the screen during key recording
     */
    public boolean isRecordingKey() {
        return false;
    }

    /**
     * Save tab (called when screen closes)
     */
    public abstract void save();
}
