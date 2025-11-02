package com.hyfata.najoan.koreanpatch.config.screen.tab;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

/**
 * 설정 화면 탭의 기본 인터페이스
 */
public abstract class SettingsTab {
    protected Screen screen;
    protected int contentStartY;
    protected int contentWidth;
    protected int contentHeight;

    /**
     * 탭 이름 (UI에 표시될 텍스트)
     */
    public abstract Component getTabName();

    /**
     * 탭 초기화
     */
    public void init(Screen screen, int contentStartY, int contentWidth, int contentHeight) {
        this.screen = screen;
        this.contentStartY = contentStartY;
        this.contentWidth = contentWidth;
        this.contentHeight = contentHeight;
        onInit();
    }

    /**
     * 탭 초기화 시 호출될 메서드 (서브클래스에서 구현)
     */
    protected abstract void onInit();

    /**
     * 렌더링
     */
    public abstract void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick);

    /**
     * 마우스 스크롤
     */
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        return false;
    }

    /**
     * 마우스 클릭
     */
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return false;
    }

    /**
     * 마우스 릴리스
     */
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return false;
    }

    /**
     * 마우스 드래그
     */
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        return false;
    }

    /**
     * 키 입력
     */
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return false;
    }

    /**
     * 키 입력 끝
     */
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        return false;
    }

    /**
     * 문자 입력
     */
    public boolean charTyped(char codePoint, int modifiers) {
        return false;
    }

    /**
     * 탭 저장 (화면 닫힐 때 호출)
     */
    public abstract void save();
}
