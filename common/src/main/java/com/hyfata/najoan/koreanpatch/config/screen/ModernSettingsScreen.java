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
 * 커스텀 모던 설정 화면
 * 탭 기반 UI로 지표, 입력, 키바인딩 설정을 관리합니다.
 */
public class ModernSettingsScreen extends Screen {
    private static final int TAB_WIDTH = 100;
    private static final int TAB_HEIGHT = 30;
    private static final int TAB_SPACING = 10;
    private static final int TAB_AREA_HEIGHT = TAB_HEIGHT + 20; // 탭과 구분선
    private static final int PADDING = 15;
    private static final int CONTENT_TOP = TAB_AREA_HEIGHT + PADDING;

    private final Screen previousScreen;
    private final List<SettingsTab> tabs = new ArrayList<>();
    private int currentTabIndex = 0;
    private int tabStartX;

    // 색상 설정
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

        // 탭 시작 X 위치 (중앙 정렬)
        int totalTabWidth = (TAB_WIDTH + TAB_SPACING) * tabs.size() - TAB_SPACING;
        this.tabStartX = (this.width - totalTabWidth) / 2;

        // 모든 탭 초기화
        for (SettingsTab tab : tabs) {
            tab.init(this, CONTENT_TOP, this.width, this.height);
        }
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        // 배경 렌더링 (renderBackground는 blur 제약이 있어서 직접 구현)
        guiGraphics.fill(0, 0, this.width, this.height, COLOR_BACKGROUND);

        // 제목 렌더링
        guiGraphics.drawCenteredString(
                this.font,
                this.title,
                this.width / 2,
                10,
                COLOR_TEXT
        );

        // 탭 렌더링
        renderTabs(guiGraphics, mouseX, mouseY);

        // 탭 구분선
        guiGraphics.fill(0, TAB_AREA_HEIGHT - 1, this.width, TAB_AREA_HEIGHT, COLOR_BORDER);

        // 현재 탭 콘텐츠 렌더링
        getCurrentTab().render(guiGraphics, mouseX, mouseY, partialTick);
    }

    /**
     * 탭 UI 렌더링
     */
    private void renderTabs(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        for (int i = 0; i < tabs.size(); i++) {
            SettingsTab tab = tabs.get(i);
            int x = tabStartX + i * (TAB_WIDTH + TAB_SPACING);
            int y = 5;

            boolean isActive = i == currentTabIndex;
            boolean isHovered = mouseX >= x && mouseX < x + TAB_WIDTH &&
                                mouseY >= y && mouseY < y + TAB_HEIGHT;

            // 탭 배경
            int bgColor = isActive ? COLOR_TAB_ACTIVE : COLOR_TAB_INACTIVE;
            guiGraphics.fill(x, y, x + TAB_WIDTH, y + TAB_HEIGHT, bgColor);

            // 액티브 탭 상단 테두리
            if (isActive) {
                guiGraphics.fill(x, y, x + TAB_WIDTH, y + 2, COLOR_TAB_ACTIVE_BORDER);
            }

            // 탭 텍스트
            int textColor = isActive ? 0xFFFFFFFF : COLOR_TEXT_SECONDARY;
            guiGraphics.drawCenteredString(
                    this.font,
                    tab.getTabName(),
                    x + TAB_WIDTH / 2,
                    y + (TAB_HEIGHT - this.font.lineHeight) / 2,
                    textColor
            );

            // 호버 효과
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

        // 탭 클릭 처리
        for (int i = 0; i < tabs.size(); i++) {
            int x = tabStartX + i * (TAB_WIDTH + TAB_SPACING);
            int y = 5;

            if (mouseX >= x && mouseX < x + TAB_WIDTH &&
                mouseY >= y && mouseY < y + TAB_HEIGHT) {
                currentTabIndex = i;
                return true;
            }
        }

        // 현재 탭에 마우스 클릭 처리
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
     * 현재 활성 탭 가져오기
     */
    private SettingsTab getCurrentTab() {
        return tabs.get(currentTabIndex);
    }

    @Override
    public void onClose() {
        // 모든 탭의 변경사항 저장
        for (SettingsTab tab : tabs) {
            tab.save();
        }
        ConfigManager.getInstance().saveConfig(ConfigManager.getInstance().getConfig());
        assert this.minecraft != null;
        this.minecraft.setScreen(previousScreen);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }
}
