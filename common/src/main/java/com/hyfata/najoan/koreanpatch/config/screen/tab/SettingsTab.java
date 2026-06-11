package com.hyfata.najoan.koreanpatch.config.screen.tab;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

/**
 * ModernSettingsScreen의 개별 탭 추상 클래스.
 * Abstract base class for each tab inside ModernSettingsScreen.
 *
 * <p>탭은 init() → render() → 입력 이벤트 순서로 동작합니다.
 * Tabs follow the lifecycle: init() → render() → input events.</p>
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

    /**
     * true일 때 ModernSettingsScreen은 ESC를 화면 닫기가 아닌 녹화 취소로 해석합니다.
     * When true, ModernSettingsScreen interprets ESC as cancelling key recording rather than closing.
     */
    public boolean isRecordingKey() {
        return false;
    }

    public void save() {
        // Override if a tab needs explicit save logic.
    }
}
