package com.hyfata.najoan.koreanpatch.config.screen.tab;

import com.hyfata.najoan.koreanpatch.config.ConfigManager;
import com.hyfata.najoan.koreanpatch.config.EasingFunctions;
import com.hyfata.najoan.koreanpatch.config.OutlineType;
import com.hyfata.najoan.koreanpatch.config.category.CategoryIndicator;
import com.hyfata.najoan.koreanpatch.config.screen.widget.WidgetUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 지표 설정 탭
 * 언어 표시기의 외형 및 동작을 설정합니다.
 */
public class IndicatorSettingsTab extends SettingsTab {
    private CategoryIndicator config;
    private int scrollOffset = 0;
    private static final int SCROLL_STEP = 15;
    private static final int SECTION_SPACING = 30;
    private static final int ITEM_HEIGHT = 25;
    private static final int TOGGLE_WIDTH = 40;
    private static final int TOGGLE_HEIGHT = 20;
    private static final int SLIDER_WIDTH = 80;
    private static final int SLIDER_HEIGHT = 15;

    // 클릭 가능한 위젯들 추적
    private final List<ClickableWidget> clickableWidgets = new ArrayList<>();
    private ClickableWidget draggingSlider = null;

    private static final Minecraft client = Minecraft.getInstance();

    // 클릭 가능한 위젯 정보
    private record ClickableWidget(int x, int y, int width, int height, WidgetType type, Runnable onClick, SliderHandler sliderHandler) {
        boolean contains(double mx, double my) {
            return mx >= x && mx < x + width && my >= y && my < y + height;
        }
    }

    private enum WidgetType { TOGGLE, SLIDER, ENUM, COLOR }

    @FunctionalInterface
    private interface SliderHandler {
        void onDrag(float value);
    }

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
        // 매 프레임마다 위젯 목록 초기화
        clickableWidgets.clear();

        int padding = 15;
        int y = contentStartY + padding - scrollOffset;
        int maxWidth = contentWidth - padding * 2;

        // "지표 표시" 섹션
        drawSection(guiGraphics, padding, y, "일반 설정", maxWidth);
        y += SECTION_SPACING;

        // 표시 여부 토글
        drawToggleSetting(guiGraphics, padding, y, "지표 표시",
                config.isShowIndicator(),
                () -> config.setShowIndicator(!config.isShowIndicator()));
        y += ITEM_HEIGHT + 10;

        // 테두리 설정 섹션
        drawSection(guiGraphics, padding, y, "테두리 설정", maxWidth);
        y += SECTION_SPACING;

        drawToggleSetting(guiGraphics, padding, y, "테두리 표시",
                config.getOutlineSettings().isShowOutline(),
                () -> config.getOutlineSettings().setShowOutline(!config.getOutlineSettings().isShowOutline()));
        y += ITEM_HEIGHT + 10;

        drawEnumSetting(guiGraphics, padding, y, "테두리 모양",
                config.getOutlineSettings().getOutlineType().name(),
                () -> {
                    OutlineType[] values = OutlineType.values();
                    int next = (config.getOutlineSettings().getOutlineType().ordinal() + 1) % values.length;
                    config.getOutlineSettings().setOutlineType(values[next]);
                });
        y += ITEM_HEIGHT + 10;

        drawColorSetting(guiGraphics, padding, y, "한국어 색상",
                config.getOutlineSettings().getColorOpacitySettings().getKoreanColor().getRGB());
        y += ITEM_HEIGHT + 10;

        drawColorSetting(guiGraphics, padding, y, "영어 색상",
                config.getOutlineSettings().getColorOpacitySettings().getEnColor().getRGB());
        y += ITEM_HEIGHT + 10;

        drawSliderSetting(guiGraphics, padding, y, "테두리 투명도",
                config.getOutlineSettings().getColorOpacitySettings().getOpacity() / 100f,
                v -> config.getOutlineSettings().getColorOpacitySettings().setOpacity((int) (v * 100)));
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
                config.getBackgroundSettings().getOpacity() / 100f,
                v -> config.getBackgroundSettings().setOpacity((int) (v * 100)));
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
                config.getTextSettings().getOpacity() / 100f,
                v -> config.getTextSettings().setOpacity((int) (v * 100)));
        y += ITEM_HEIGHT + 10;

        // 애니메이션 설정 섹션
        drawSection(guiGraphics, padding, y, "애니메이션 설정", maxWidth);
        y += SECTION_SPACING;

        drawToggleSetting(guiGraphics, padding, y, "애니메이션",
                config.getAnimationSettings().isShowAnimation(),
                () -> config.getAnimationSettings().setShowAnimation(!config.getAnimationSettings().isShowAnimation()));
        y += ITEM_HEIGHT + 10;

        drawEnumSetting(guiGraphics, padding, y, "이징 함수",
                config.getAnimationSettings().getEasingFunction().name(),
                () -> {
                    EasingFunctions[] values = EasingFunctions.values();
                    int next = (config.getAnimationSettings().getEasingFunction().ordinal() + 1) % values.length;
                    config.getAnimationSettings().setEasingFunction(values[next]);
                });
        y += ITEM_HEIGHT + 10;

        drawSliderSetting(guiGraphics, padding, y, "애니메이션 속도",
                config.getAnimationSettings().getSpeed() / 100f,
                v -> config.getAnimationSettings().setSpeed((int) (v * 100)));
    }

    /**
     * 전체 콘텐츠 높이 계산
     */
    private int getContentHeight() {
        int padding = 15;
        int height = padding;
        // 일반 설정: 섹션 + 토글 1개
        height += SECTION_SPACING + (ITEM_HEIGHT + 10);
        // 테두리 설정: 섹션 + 토글 1 + enum 1 + 색상 2 + 슬라이더 1
        height += SECTION_SPACING + (ITEM_HEIGHT + 10) * 5;
        // 배경 설정: 섹션 + 색상 2 + 슬라이더 1
        height += SECTION_SPACING + (ITEM_HEIGHT + 10) * 3;
        // 텍스트 설정: 섹션 + 색상 2 + 슬라이더 1
        height += SECTION_SPACING + (ITEM_HEIGHT + 10) * 3;
        // 애니메이션 설정: 섹션 + 토글 1 + enum 1 + 슬라이더 1
        height += SECTION_SPACING + (ITEM_HEIGHT + 10) * 3;
        return height;
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
    private void drawToggleSetting(GuiGraphics guiGraphics, int x, int y, String label, boolean value, Runnable onClick) {
        guiGraphics.drawString(client.font, label, x, y, WidgetUtils.COLOR_TEXT, false);
        int toggleX = x + 250;
        WidgetUtils.drawToggle(guiGraphics, toggleX, y, value);
        // 위젯 등록
        clickableWidgets.add(new ClickableWidget(toggleX, y, TOGGLE_WIDTH, TOGGLE_HEIGHT, WidgetType.TOGGLE, onClick, null));
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
        guiGraphics.drawString(client.font, value, enumX + 5, y, WidgetUtils.COLOR_TEXT_SECONDARY, false);
        // 위젯 등록
        clickableWidgets.add(new ClickableWidget(enumX, y - 2, enumWidth, 20, WidgetType.ENUM, onClick, null));
    }

    /**
     * 색상 설정 항목 렌더링
     */
    private void drawColorSetting(GuiGraphics guiGraphics, int x, int y, String label, int color) {
        guiGraphics.drawString(client.font, label, x, y, WidgetUtils.COLOR_TEXT, false);
        WidgetUtils.drawColorBox(guiGraphics, x + 250, y, 20, color);
        // 색상 선택기는 추후 구현 (현재는 클릭 이벤트 없음)
    }

    /**
     * 슬라이더 설정 항목 렌더링
     */
    private void drawSliderSetting(GuiGraphics guiGraphics, int x, int y, String label, float value, SliderHandler handler) {
        guiGraphics.drawString(client.font, label, x, y, WidgetUtils.COLOR_TEXT, false);
        int sliderX = x + 250;
        int sliderY = y + 3;
        WidgetUtils.drawSlider(guiGraphics, sliderX, sliderY, SLIDER_WIDTH, SLIDER_HEIGHT,
                value, WidgetUtils.COLOR_WIDGET_BG, WidgetUtils.COLOR_ACCENT);
        String percentText = String.format("%.0f%%", value * 100);
        guiGraphics.drawString(client.font, percentText, sliderX + SLIDER_WIDTH + 5, y, WidgetUtils.COLOR_TEXT_SECONDARY, false);
        // 위젯 등록
        clickableWidgets.add(new ClickableWidget(sliderX, sliderY, SLIDER_WIDTH, SLIDER_HEIGHT, WidgetType.SLIDER, null, handler));
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button != 0) return false;

        for (ClickableWidget widget : clickableWidgets) {
            if (widget.contains(mouseX, mouseY)) {
                if (widget.type == WidgetType.SLIDER && widget.sliderHandler != null) {
                    // 슬라이더 드래그 시작
                    draggingSlider = widget;
                    float value = (float) Math.max(0, Math.min(1, (mouseX - widget.x) / widget.width));
                    widget.sliderHandler.onDrag(value);
                    return true;
                } else if (widget.onClick != null) {
                    widget.onClick.run();
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (draggingSlider != null) {
            draggingSlider = null;
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (draggingSlider != null && draggingSlider.sliderHandler != null) {
            float value = (float) Math.max(0, Math.min(1, (mouseX - draggingSlider.x) / draggingSlider.width));
            draggingSlider.sliderHandler.onDrag(value);
            return true;
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
