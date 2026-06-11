package com.hyfata.najoan.koreanpatch.config.screen.tab;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

/**
 * Base class for settings screen tabs
 */
public abstract class SettingsTab {
    protected Screen screen;
    protected int contentStartY;
    protected int contentWidth;
    protected int contentHeight;

    public abstract Component getTabName();

    public void init(Screen screen, int contentStartY, int contentWidth, int contentHeight) {
        this.screen = screen;
        this.contentStartY = contentStartY;
        this.contentWidth = contentWidth;
        this.contentHeight = contentHeight;
        onInit();
    }

    protected abstract void onInit();

    public abstract void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick);

    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        return false;
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return false;
    }

    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return false;
    }

    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        return false;
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return false;
    }

    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        return false;
    }

    public boolean charTyped(char codePoint, int modifiers) {
        return false;
    }

    public boolean isRecordingKey() {
        return false;
    }
}
