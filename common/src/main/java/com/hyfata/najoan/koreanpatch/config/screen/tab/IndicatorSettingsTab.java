package com.hyfata.najoan.koreanpatch.config.screen.tab;

import com.hyfata.najoan.koreanpatch.config.ConfigManager;
import com.hyfata.najoan.koreanpatch.config.category.CategoryIndicator;
import com.hyfata.najoan.koreanpatch.config.screen.widget.WidgetUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

/**
 * 지표 설정 탭
 * 언어 표시기의 외형 및 동작을 설정합니다.
 */
public class IndicatorSettingsTab extends SettingsTab {
    private CategoryIndicator config;
    private int scrollY = 0;
    private static final int SCROLL_STEP = 15;
    private static final int SECTION_SPACING = 30;
    private static final int ITEM_HEIGHT = 25;

    private static final Minecraft client = Minecraft.getInstance();

    @Override
    public Component getTabName() {
        return Component.literal("지표");
    }

    @Override
    protected void onInit() {
        this.config = ConfigManager.getInstance().getConfig().getCategoryIndicator();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int padding = 15;
        int y = contentStartY + padding - scrollY;
        int maxWidth = contentWidth - padding * 2;

        // "지표 표시" 섹션
        drawSection(guiGraphics, padding, y, "일반 설정", maxWidth);
        y += SECTION_SPACING;

        // 표시 여부 토글
        drawToggleSetting(guiGraphics, padding, y, "지표 표시",
                config.isShowIndicator());
        y += ITEM_HEIGHT + 10;

        // 테두리 설정 섹션
        drawSection(guiGraphics, padding, y, "테두리 설정", maxWidth);
        y += SECTION_SPACING;

        drawToggleSetting(guiGraphics, padding, y, "테두리 표시",
                config.getOutlineSettings().isShowOutline());
        y += ITEM_HEIGHT + 10;

        drawEnumSetting(guiGraphics, padding, y, "테두리 모양",
                config.getOutlineSettings().getOutlineType().name());
        y += ITEM_HEIGHT + 10;

        drawColorSetting(guiGraphics, padding, y, "한국어 색상",
                config.getOutlineSettings().getColorOpacitySettings().getKoreanColor().getRGB());
        y += ITEM_HEIGHT + 10;

        drawColorSetting(guiGraphics, padding, y, "영어 색상",
                config.getOutlineSettings().getColorOpacitySettings().getEnColor().getRGB());
        y += ITEM_HEIGHT + 10;

        drawSliderSetting(guiGraphics, padding, y, "테두리 투명도",
                config.getOutlineSettings().getColorOpacitySettings().getOpacity() / 100f);
        y += ITEM_HEIGHT + 10;

        // 배경 설정 섹션
        drawSection(guiGraphics, padding, y, "배경 설정", maxWidth);
        y += SECTION_SPACING;

        drawColorSetting(guiGraphics, padding, y, "한국어 배경색",
                config.getBackgroundSettings().getKoreanColor().getRGB());
        y += ITEM_HEIGHT + 10;

        drawColorSetting(guiGraphics, padding, y, "영어 배경색",
                config.getBackgroundSettings().getEnColor().getRGB());
        y += ITEM_HEIGHT + 10;

        drawSliderSetting(guiGraphics, padding, y, "배경 투명도",
                config.getBackgroundSettings().getOpacity() / 100f);
        y += ITEM_HEIGHT + 10;

        // 텍스트 설정 섹션
        drawSection(guiGraphics, padding, y, "텍스트 설정", maxWidth);
        y += SECTION_SPACING;

        drawColorSetting(guiGraphics, padding, y, "한국어 텍스트",
                config.getTextSettings().getKoreanColor().getRGB());
        y += ITEM_HEIGHT + 10;

        drawColorSetting(guiGraphics, padding, y, "영어 텍스트",
                config.getTextSettings().getEnColor().getRGB());
        y += ITEM_HEIGHT + 10;

        drawSliderSetting(guiGraphics, padding, y, "텍스트 투명도",
                config.getTextSettings().getOpacity() / 100f);
        y += ITEM_HEIGHT + 10;

        // 애니메이션 설정 섹션
        drawSection(guiGraphics, padding, y, "애니메이션 설정", maxWidth);
        y += SECTION_SPACING;

        drawToggleSetting(guiGraphics, padding, y, "애니메이션",
                config.getAnimationSettings().isShowAnimation());
        y += ITEM_HEIGHT + 10;

        drawEnumSetting(guiGraphics, padding, y, "이징 함수",
                config.getAnimationSettings().getEasingFunction().name());
        y += ITEM_HEIGHT + 10;

        drawSliderSetting(guiGraphics, padding, y, "애니메이션 속도",
                config.getAnimationSettings().getSpeed() / 100f);
    }

    /**
     * 섹션 제목 렌더링
     */
    private void drawSection(GuiGraphics guiGraphics, int x, int y, String title, int width) {
        WidgetUtils.drawSectionTitle(guiGraphics, x, y, title);
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
        guiGraphics.drawString(client.font, value, x + 250, y, WidgetUtils.COLOR_TEXT_SECONDARY, false);
    }

    /**
     * 색상 설정 항목 렌더링
     */
    private void drawColorSetting(GuiGraphics guiGraphics, int x, int y, String label, int color) {
        guiGraphics.drawString(client.font, label, x, y, WidgetUtils.COLOR_TEXT, false);
        WidgetUtils.drawColorBox(guiGraphics, x + 250, y, 20, color);
    }

    /**
     * 슬라이더 설정 항목 렌더링
     */
    private void drawSliderSetting(GuiGraphics guiGraphics, int x, int y, String label, float value) {
        guiGraphics.drawString(client.font, label, x, y, WidgetUtils.COLOR_TEXT, false);
        WidgetUtils.drawSlider(guiGraphics, x + 250, y + 3, 80, 15,
                value, WidgetUtils.COLOR_WIDGET_BG, WidgetUtils.COLOR_ACCENT);
        String percentText = String.format("%.0f%%", value * 100);
        guiGraphics.drawString(client.font, percentText, x + 335, y, WidgetUtils.COLOR_TEXT_SECONDARY, false);
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
