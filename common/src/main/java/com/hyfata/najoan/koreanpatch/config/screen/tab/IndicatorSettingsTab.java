package com.hyfata.najoan.koreanpatch.config.screen.tab;

import com.hyfata.najoan.koreanpatch.config.ConfigManager;
import com.hyfata.najoan.koreanpatch.config.EasingFunctions;
import com.hyfata.najoan.koreanpatch.config.OutlineType;
import com.hyfata.najoan.koreanpatch.config.category.CategoryIndicator;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class IndicatorSettingsTab extends SettingsTab {
    private CategoryIndicator config;
    private final List<TabRenderHelper.ClickableWidget> clickableWidgets = new ArrayList<>();
    private TabRenderHelper.ClickableWidget draggingSlider = null;
    private TabScrollHandler scroll;
    private int lastContentHeight;

    @Override
    public Component getTabName() {
        return Component.translatable("koreanpatch.config.tab.indicator");
    }

    @Override
    protected void onInit() {
        this.config = ConfigManager.getInstance().getConfig().getCategoryIndicator();
        this.scroll = new TabScrollHandler(contentWidth, contentStartY, contentHeight);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        clickableWidgets.clear();

        int x = TabRenderHelper.contentX(contentWidth);
        int y = TabRenderHelper.startY(contentStartY, scroll.getScrollOffset());

        // General settings
        y = TabRenderHelper.drawSection(guiGraphics, x, y,
                Component.translatable("koreanpatch.config.indicator.general").getString(),
                TabRenderHelper.CONTENT_WIDTH);

        y = TabRenderHelper.drawToggleSetting(guiGraphics, x, y,
                Component.translatable("koreanpatch.config.indicator.general.show").getString(),
                config.isShowIndicator(),
                () -> config.setShowIndicator(!config.isShowIndicator()),
                clickableWidgets);

        y += TabRenderHelper.SECTION_GAP;

        // Outline settings
        y = TabRenderHelper.drawSection(guiGraphics, x, y,
                Component.translatable("koreanpatch.config.indicator.outline").getString(),
                TabRenderHelper.CONTENT_WIDTH);

        y = TabRenderHelper.drawToggleSetting(guiGraphics, x, y,
                Component.translatable("koreanpatch.config.indicator.outline.show").getString(),
                config.getOutlineSettings().isShowOutline(),
                () -> config.getOutlineSettings().setShowOutline(!config.getOutlineSettings().isShowOutline()),
                clickableWidgets);

        y = TabRenderHelper.drawEnumSetting(guiGraphics, x, y,
                Component.translatable("koreanpatch.config.indicator.outline.outline_type").getString(),
                config.getOutlineSettings().getOutlineType().name(),
                () -> {
                    OutlineType[] values = OutlineType.values();
                    int next = (config.getOutlineSettings().getOutlineType().ordinal() + 1) % values.length;
                    config.getOutlineSettings().setOutlineType(values[next]);
                },
                clickableWidgets);

        y = TabRenderHelper.drawColorSetting(guiGraphics, x, y,
                Component.translatable("koreanpatch.config.color_opacity.ko").getString(),
                config.getOutlineSettings().getColorOpacitySettings().getKoreanColor().getRGB());

        y = TabRenderHelper.drawColorSetting(guiGraphics, x, y,
                Component.translatable("koreanpatch.config.color_opacity.en").getString(),
                config.getOutlineSettings().getColorOpacitySettings().getEnColor().getRGB());

        y = TabRenderHelper.drawSliderSetting(guiGraphics, x, y,
                Component.translatable("koreanpatch.config.color_opacity.opacity").getString(),
                config.getOutlineSettings().getColorOpacitySettings().getOpacity() / 100f,
                v -> config.getOutlineSettings().getColorOpacitySettings().setOpacity((int) (v * 100)),
                clickableWidgets);

        y += TabRenderHelper.SECTION_GAP;

        // Background settings
        y = TabRenderHelper.drawSection(guiGraphics, x, y,
                Component.translatable("koreanpatch.config.indicator.background").getString(),
                TabRenderHelper.CONTENT_WIDTH);

        y = TabRenderHelper.drawColorSetting(guiGraphics, x, y,
                Component.translatable("koreanpatch.config.color_opacity.ko").getString(),
                config.getBackgroundSettings().getKoreanColor().getRGB());

        y = TabRenderHelper.drawColorSetting(guiGraphics, x, y,
                Component.translatable("koreanpatch.config.color_opacity.en").getString(),
                config.getBackgroundSettings().getEnColor().getRGB());

        y = TabRenderHelper.drawSliderSetting(guiGraphics, x, y,
                Component.translatable("koreanpatch.config.color_opacity.opacity").getString(),
                config.getBackgroundSettings().getOpacity() / 100f,
                v -> config.getBackgroundSettings().setOpacity((int) (v * 100)),
                clickableWidgets);

        y += TabRenderHelper.SECTION_GAP;

        // Text settings
        y = TabRenderHelper.drawSection(guiGraphics, x, y,
                Component.translatable("koreanpatch.config.indicator.text").getString(),
                TabRenderHelper.CONTENT_WIDTH);

        y = TabRenderHelper.drawColorSetting(guiGraphics, x, y,
                Component.translatable("koreanpatch.config.color_opacity.ko").getString(),
                config.getTextSettings().getKoreanColor().getRGB());

        y = TabRenderHelper.drawColorSetting(guiGraphics, x, y,
                Component.translatable("koreanpatch.config.color_opacity.en").getString(),
                config.getTextSettings().getEnColor().getRGB());

        y = TabRenderHelper.drawSliderSetting(guiGraphics, x, y,
                Component.translatable("koreanpatch.config.color_opacity.opacity").getString(),
                config.getTextSettings().getOpacity() / 100f,
                v -> config.getTextSettings().setOpacity((int) (v * 100)),
                clickableWidgets);

        y += TabRenderHelper.SECTION_GAP;

        // Animation settings
        y = TabRenderHelper.drawSection(guiGraphics, x, y,
                Component.translatable("koreanpatch.config.indicator.animation").getString(),
                TabRenderHelper.CONTENT_WIDTH);

        y = TabRenderHelper.drawToggleSetting(guiGraphics, x, y,
                Component.translatable("koreanpatch.config.indicator.animation.show").getString(),
                config.getAnimationSettings().isShowAnimation(),
                () -> config.getAnimationSettings().setShowAnimation(!config.getAnimationSettings().isShowAnimation()),
                clickableWidgets);

        y = TabRenderHelper.drawEnumSetting(guiGraphics, x, y,
                Component.translatable("koreanpatch.config.indicator.animation.easing_function").getString(),
                config.getAnimationSettings().getEasingFunction().name(),
                () -> {
                    EasingFunctions[] values = EasingFunctions.values();
                    int next = (config.getAnimationSettings().getEasingFunction().ordinal() + 1) % values.length;
                    config.getAnimationSettings().setEasingFunction(values[next]);
                },
                clickableWidgets);

        y = TabRenderHelper.drawSliderSetting(guiGraphics, x, y,
                Component.translatable("koreanpatch.config.indicator.animation.speed").getString(),
                config.getAnimationSettings().getSpeed() / 100f,
                v -> config.getAnimationSettings().setSpeed((int) (v * 100)),
                clickableWidgets);

        lastContentHeight = y + scroll.getScrollOffset() - (contentStartY + 15);
        scroll.renderScrollbar(guiGraphics, lastContentHeight);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button != 0) return false;
        if (scroll.handleMouseClicked(mouseX, mouseY, lastContentHeight)) return true;
        return TabRenderHelper.handleWidgetClick(clickableWidgets, mouseX, mouseY, slider -> draggingSlider = slider);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (scroll.handleMouseReleased()) return true;
        if (draggingSlider != null) {
            draggingSlider = null;
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (scroll.handleMouseDragged(mouseY, lastContentHeight)) return true;
        return TabRenderHelper.handleSliderDrag(draggingSlider, mouseX);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        return scroll.handleMouseScrolled(scrollY, lastContentHeight);
    }
}
