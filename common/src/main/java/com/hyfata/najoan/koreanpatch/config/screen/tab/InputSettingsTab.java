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
 * 텍스트 입력 및 IME 동작 설정 탭.
 * Text input and IME behavior settings tab.
 */
public class InputSettingsTab extends SettingsTab {
    private CategoryInput config;
    private final List<TabRenderHelper.ClickableWidget> clickableWidgets = new ArrayList<>();
    private TabScrollHandler scroll;
    private int lastContentHeight;

    private static final Minecraft CLIENT = Minecraft.getInstance();

    @Override
    public Component getTabName() {
        return Component.translatable("koreanpatch.config.tab.input");
    }

    @Override
    protected void onInit() {
        this.config = ConfigManager.getInstance().getConfig().getCategoryInput();
        this.scroll = new TabScrollHandler(contentWidth, contentStartY, contentHeight);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        clickableWidgets.clear();

        int x = TabRenderHelper.contentX(contentWidth);
        int y = TabRenderHelper.startY(contentStartY, scroll.getScrollOffset());

        // General settings
        y = TabRenderHelper.drawSection(guiGraphics, x, y,
                Component.translatable("koreanpatch.config.input.general").getString(),
                TabRenderHelper.CONTENT_WIDTH);

        y = TabRenderHelper.drawEnumSetting(guiGraphics, x, y,
                Component.translatable("koreanpatch.config.input.general.auto_lang_type_mode").getString(),
                getAutoLangTypeModeName(config.getAutoLangTypeMode()),
                () -> {
                    AutoLangTypeMode[] values = AutoLangTypeMode.values();
                    int next = (config.getAutoLangTypeMode().ordinal() + 1) % values.length;
                    config.setAutoLangTypeMode(values[next]);
                },
                clickableWidgets);

        y = TabRenderHelper.drawToggleSetting(guiGraphics, x, y,
                Component.translatable("koreanpatch.config.input.general.memory_lang_type").getString(),
                config.isMemoryLangTypePerScreen(),
                () -> config.setMemoryLangTypePerScreen(!config.isMemoryLangTypePerScreen()),
                clickableWidgets);

        y += TabRenderHelper.SECTION_GAP;

        // IME settings
        y = TabRenderHelper.drawSection(guiGraphics, x, y,
                Component.translatable("koreanpatch.config.input.ime").getString(),
                TabRenderHelper.CONTENT_WIDTH);

        y = TabRenderHelper.drawToggleSetting(guiGraphics, x, y,
                Component.translatable("koreanpatch.config.input.ime.disable_ime_playing").getString(),
                config.isDisableImeWhenPlaying(),
                () -> config.setDisableImeWhenPlaying(!config.isDisableImeWhenPlaying()),
                clickableWidgets);

        y = TabRenderHelper.drawToggleSetting(guiGraphics, x, y,
                Component.translatable("koreanpatch.config.input.ime.auto_ime_switch").getString(),
                config.isAutoImeSwitch(),
                () -> config.setAutoImeSwitch(!config.isAutoImeSwitch()),
                clickableWidgets);

        y = TabRenderHelper.drawToggleSetting(guiGraphics, x, y,
                Component.translatable("koreanpatch.config.input.ime.always_ime_enabled").getString(),
                config.isAlwaysImeEnabled(),
                () -> config.setAlwaysImeEnabled(!config.isAlwaysImeEnabled()),
                clickableWidgets);

        y += TabRenderHelper.SECTION_GAP;

        // Information
        y = TabRenderHelper.drawSection(guiGraphics, x, y, "Information", TabRenderHelper.CONTENT_WIDTH);

        guiGraphics.drawString(
                CLIENT.font,
                Component.translatable("koreanpatch.config.input.info").getString(),
                x, y, WidgetUtils.COLOR_TEXT_SECONDARY, false
        );

        lastContentHeight = y + scroll.getScrollOffset() - (contentStartY + 15) + CLIENT.font.lineHeight;
        scroll.renderScrollbar(guiGraphics, lastContentHeight);
    }

    private String getAutoLangTypeModeName(AutoLangTypeMode mode) {
        return switch (mode) {
            case AUTO -> Component.translatable("koreanpatch.config.input.mode.auto").getString();
            case KOREAN -> Component.translatable("koreanpatch.config.input.mode.korean").getString();
            case ENGLISH -> Component.translatable("koreanpatch.config.input.mode.english").getString();
            case IME -> Component.translatable("koreanpatch.config.input.mode.ime").getString();
        };
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button != 0) return false;
        if (scroll.handleMouseClicked(mouseX, mouseY, lastContentHeight)) return true;
        return TabRenderHelper.handleWidgetClick(clickableWidgets, mouseX, mouseY, null);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return scroll.handleMouseReleased();
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        return scroll.handleMouseDragged(mouseY, lastContentHeight);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        return scroll.handleMouseScrolled(scrollY, lastContentHeight);
    }
}
