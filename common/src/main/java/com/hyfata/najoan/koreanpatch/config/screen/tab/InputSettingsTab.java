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
 * 입력 설정 탭
 * 텍스트 입력 동작 및 IME 관련 설정을 관리합니다.
 */
public class InputSettingsTab extends SettingsTab {
    private CategoryInput config;
    private int scrollOffset = 0;
    private static final int SCROLL_STEP = 15;
    private static final int SECTION_SPACING = 30;
    private static final int ITEM_HEIGHT = 25;
    private static final int TOGGLE_WIDTH = 40;
    private static final int TOGGLE_HEIGHT = 20;

    // 클릭 가능한 위젯들 추적
    private final List<ClickableWidget> clickableWidgets = new ArrayList<>();

    private static final Minecraft client = Minecraft.getInstance();

    // 클릭 가능한 위젯 정보
    private record ClickableWidget(int x, int y, int width, int height, Runnable onClick) {
        boolean contains(double mx, double my) {
            return mx >= x && mx < x + width && my >= y && my < y + height;
        }
    }

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
        // 매 프레임마다 위젯 목록 초기화
        clickableWidgets.clear();

        int padding = 15;
        int y = contentStartY + padding - scrollOffset;
        int maxWidth = contentWidth - padding * 2;

        // 일반 설정 섹션
        drawSection(guiGraphics, padding, y, "일반 설정", maxWidth);
        y += SECTION_SPACING;

        // 자동 언어 타입 모드
        drawEnumSetting(guiGraphics, padding, y, "자동 언어 모드",
                config.getAutoLangTypeMode().name(),
                () -> {
                    AutoLangTypeMode[] values = AutoLangTypeMode.values();
                    int next = (config.getAutoLangTypeMode().ordinal() + 1) % values.length;
                    config.setAutoLangTypeMode(values[next]);
                });
        y += ITEM_HEIGHT + 15;

        // 화면별 언어 기억
        drawToggleSetting(guiGraphics, padding, y, "화면별 언어 상태 기억",
                config.isMemoryLangTypePerScreen(),
                () -> config.setMemoryLangTypePerScreen(!config.isMemoryLangTypePerScreen()));
        y += ITEM_HEIGHT + 10;

        // IME 설정 섹션
        drawSection(guiGraphics, padding, y, "IME 설정", maxWidth);
        y += SECTION_SPACING;

        // 게임 중 IME 비활성화
        drawToggleSetting(guiGraphics, padding, y, "게임 중 IME 비활성화",
                config.isDisableImeWhenPlaying(),
                () -> config.setDisableImeWhenPlaying(!config.isDisableImeWhenPlaying()));
        y += ITEM_HEIGHT + 10;

        // 자동 IME 전환
        drawToggleSetting(guiGraphics, padding, y, "자동 IME 전환",
                config.isAutoImeSwitch(),
                () -> config.setAutoImeSwitch(!config.isAutoImeSwitch()));
        y += ITEM_HEIGHT + 10;

        // 항상 IME 활성화
        drawToggleSetting(guiGraphics, padding, y, "항상 IME 활성화",
                config.isAlwaysImeEnabled(),
                () -> config.setAlwaysImeEnabled(!config.isAlwaysImeEnabled()));
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
     * 전체 콘텐츠 높이 계산
     */
    private int getContentHeight() {
        int padding = 15;
        int height = padding;
        // 일반 설정: 섹션 + enum 1 + 토글 1
        height += SECTION_SPACING + (ITEM_HEIGHT + 15) + (ITEM_HEIGHT + 10);
        // IME 설정: 섹션 + 토글 3
        height += SECTION_SPACING + (ITEM_HEIGHT + 10) * 3;
        // 정보: 섹션 + 텍스트
        height += SECTION_SPACING + ITEM_HEIGHT;
        return height;
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
    private void drawToggleSetting(GuiGraphics guiGraphics, int x, int y, String label, boolean value, Runnable onClick) {
        guiGraphics.drawString(client.font, label, x, y, WidgetUtils.COLOR_TEXT, false);
        int toggleX = x + 250;
        WidgetUtils.drawToggle(guiGraphics, toggleX, y, value);
        // 위젯 등록
        clickableWidgets.add(new ClickableWidget(toggleX, y, TOGGLE_WIDTH, TOGGLE_HEIGHT, onClick));
    }

    /**
     * 열거형 설정 항목 렌더링
     */
    private void drawEnumSetting(GuiGraphics guiGraphics, int x, int y, String label, String value, Runnable onClick) {
        guiGraphics.drawString(client.font, label, x, y, WidgetUtils.COLOR_TEXT, false);
        int enumX = x + 250;
        int enumWidth = 100;
        WidgetUtils.drawBorderedRect(guiGraphics, enumX, y - 2, enumWidth, 20,
                WidgetUtils.COLOR_WIDGET_BG, WidgetUtils.COLOR_BORDER);
        guiGraphics.drawString(client.font, value, enumX + 5, y,
                WidgetUtils.COLOR_TEXT_SECONDARY, false);
        // 위젯 등록
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
        // 설정은 실시간으로 저장되거나 부모에서 저장됨
    }
}
