package com.hyfata.najoan.koreanpatch.config.screen.tab;

import com.hyfata.najoan.koreanpatch.config.ConfigManager;
import com.hyfata.najoan.koreanpatch.config.category.CategoryInput;
import com.hyfata.najoan.koreanpatch.config.screen.widget.WidgetUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

/**
 * 입력 설정 탭
 * 텍스트 입력 동작 및 IME 관련 설정을 관리합니다.
 */
public class InputSettingsTab extends SettingsTab {
    private CategoryInput config;
    private int scrollY = 0;
    private static final int SCROLL_STEP = 15;
    private static final int SECTION_SPACING = 30;
    private static final int ITEM_HEIGHT = 25;

    private static final Minecraft client = Minecraft.getInstance();

    @Override
    public Component getTabName() {
        return Component.literal("입력");
    }

    @Override
    protected void onInit() {
        this.config = ConfigManager.getInstance().getConfig().getCategoryInput();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int padding = 15;
        int y = contentStartY + padding - scrollY;
        int maxWidth = contentWidth - padding * 2;

        // 일반 설정 섹션
        drawSection(guiGraphics, padding, y, "일반 설정", maxWidth);
        y += SECTION_SPACING;

        // 자동 언어 타입 모드
        drawEnumSetting(guiGraphics, padding, y, "자동 언어 모드",
                config.getAutoLangTypeMode().name());
        y += ITEM_HEIGHT + 15;

        // 화면별 언어 기억
        drawToggleSetting(guiGraphics, padding, y, "화면별 언어 상태 기억",
                config.isMemoryLangTypePerScreen());
        y += ITEM_HEIGHT + 10;

        // IME 설정 섹션
        drawSection(guiGraphics, padding, y, "IME 설정", maxWidth);
        y += SECTION_SPACING;

        // 게임 중 IME 비활성화
        drawToggleSetting(guiGraphics, padding, y, "게임 중 IME 비활성화",
                config.isDisableImeWhenPlaying());
        y += ITEM_HEIGHT + 10;

        // 자동 IME 전환
        drawToggleSetting(guiGraphics, padding, y, "자동 IME 전환",
                config.isAutoImeSwitch());
        y += ITEM_HEIGHT + 10;

        // 항상 IME 활성화
        drawToggleSetting(guiGraphics, padding, y, "항상 IME 활성화",
                config.isAlwaysImeEnabled());
        y += ITEM_HEIGHT + 10;

        // 정보 섹션
        drawSection(guiGraphics, padding, y, "정보", maxWidth);
        y += SECTION_SPACING;

        guiGraphics.drawString(
                client.font,
                "자동 언어 모드는 한/영 변환키 설정 탭에서 변경할 수 있습니다.",
                padding, y, WidgetUtils.COLOR_TEXT_SECONDARY, false
        );
    }

    /**
     * 섹션 제목 렌더링
     */
    private void drawSection(GuiGraphics guiGraphics, int x, int y, String title, int width) {
        guiGraphics.drawString(client.font, title, x, y, WidgetUtils.COLOR_TEXT, false);
        guiGraphics.fill(x, y + 15, x + width, y + 16, WidgetUtils.COLOR_BORDER);
    }

    /**
     * 토글 설정 항목 렌더링
     */
    private void drawToggleSetting(GuiGraphics guiGraphics, int x, int y, String label, boolean value) {
        guiGraphics.drawString(client.font, label, x, y, WidgetUtils.COLOR_TEXT, false);
        WidgetUtils.drawToggle(guiGraphics, x + 250, y, value);
    }

    /**
     * 열거형 설정 항목 렌더링
     */
    private void drawEnumSetting(GuiGraphics guiGraphics, int x, int y, String label, String value) {
        guiGraphics.drawString(client.font, label, x, y, WidgetUtils.COLOR_TEXT, false);
        WidgetUtils.drawBorderedRect(guiGraphics, x + 250, y - 2, 100, 20,
                WidgetUtils.COLOR_WIDGET_BG, WidgetUtils.COLOR_BORDER);
        guiGraphics.drawString(client.font, value, x + 260, y,
                WidgetUtils.COLOR_TEXT_SECONDARY, false);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (scrollY > 0) {
            this.scrollY = Math.max(0, (int) (this.scrollY - scrollY * SCROLL_STEP));
            return true;
        }
        return false;
    }

    @Override
    public void save() {
        // 설정은 실시간으로 저장되거나 부모에서 저장됨
    }
}
