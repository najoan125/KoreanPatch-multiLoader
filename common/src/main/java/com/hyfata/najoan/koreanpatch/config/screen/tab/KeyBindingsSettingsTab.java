package com.hyfata.najoan.koreanpatch.config.screen.tab;

import com.hyfata.najoan.koreanpatch.config.ConfigManager;
import com.hyfata.najoan.koreanpatch.config.category.CategoryKeyBindings;
import com.hyfata.najoan.koreanpatch.config.screen.widget.WidgetUtils;
import com.hyfata.najoan.koreanpatch.keybinding.KeyCombination;
import com.hyfata.najoan.koreanpatch.keybinding.KeyIdentifier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeyBindingsSettingsTab extends SettingsTab {
    private CategoryKeyBindings config;
    private TabScrollHandler scroll;
    private int lastContentHeight;

    private static final int CONTENT_WIDTH = 380;
    private static final int ITEM_HEIGHT = 30;
    private static final int BUTTON_WIDTH = 70;
    private static final int BUTTON_HEIGHT = 20;
    private static final int ITEM_GAP = 5;
    private static final int SECTION_SPACING = 25;
    private static final int SECTION_GAP = 15;

    private int recordingKeyIndex = -1;
    private int recordingType = -1;
    private final Map<Integer, KeyIdentifier> pressedKeys = new HashMap<>();
    private final List<KeyIdentifier> recordedKeyCombo = new ArrayList<>();
    private long recordingStartTime = 0;
    private static final long RECORDING_TIMEOUT = 5000;

    private static final Minecraft CLIENT = Minecraft.getInstance();

    @Override
    public Component getTabName() {
        return Component.translatable("koreanpatch.config.tab.keybindings");
    }

    @Override
    protected void onInit() {
        this.config = ConfigManager.getInstance().getConfig().getCategoryKeyBindings();
        this.scroll = new TabScrollHandler(contentWidth, contentStartY, contentHeight);
    }

    @Override
    public boolean isRecordingKey() {
        return recordingKeyIndex >= 0;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int x = (contentWidth - CONTENT_WIDTH) / 2;
        int y = contentStartY + 15 - scroll.getScrollOffset();

        // Language toggle keys
        y = TabRenderHelper.drawSection(guiGraphics, x, y,
                Component.translatable("koreanpatch.config.keybindings.langtype").getString(),
                CONTENT_WIDTH);

        List<List<KeyIdentifier>> langTypeKeys = config.getLangTypeKeys();
        for (int i = 0; i < 2; i++) {
            String label = Component.translatable("koreanpatch.config.keybindings.key", i + 1).getString();
            String display = (i < langTypeKeys.size())
                    ? new KeyCombination(langTypeKeys.get(i)).getDisplayName()
                    : Component.translatable("koreanpatch.config.keybindings.notset").getString();
            drawKeyBindingItem(guiGraphics, x, y, label, display, 0, i, mouseX, mouseY);
            y += ITEM_HEIGHT + ITEM_GAP;
        }

        y += SECTION_GAP;

        // IME toggle keys
        y = TabRenderHelper.drawSection(guiGraphics, x, y,
                Component.translatable("koreanpatch.config.keybindings.ime").getString(),
                CONTENT_WIDTH);

        List<List<KeyIdentifier>> imeKeys = config.getImeKeys();
        for (int i = 0; i < 1; i++) {
            String label = Component.translatable("koreanpatch.config.keybindings.key", 1).getString();
            String display = (i < imeKeys.size())
                    ? new KeyCombination(imeKeys.get(i)).getDisplayName()
                    : Component.translatable("koreanpatch.config.keybindings.notset").getString();
            drawKeyBindingItem(guiGraphics, x, y, label, display, 1, i, mouseX, mouseY);
            y += ITEM_HEIGHT + ITEM_GAP;
        }

        // Recording overlay
        if (recordingKeyIndex >= 0) {
            long elapsed = System.currentTimeMillis() - recordingStartTime;
            if (elapsed > RECORDING_TIMEOUT) {
                cancelKeyRecording();
            } else {
                y = renderRecordingOverlay(guiGraphics, x, y, elapsed);
            }
        }

        lastContentHeight = y + scroll.getScrollOffset() - (contentStartY + 15);
        scroll.renderScrollbar(guiGraphics, lastContentHeight);
    }

    private int renderRecordingOverlay(GuiGraphics guiGraphics, int x, int y, long elapsed) {
        y += 20;
        y = TabRenderHelper.drawSection(guiGraphics, x, y,
                Component.translatable("koreanpatch.config.keybindings.waiting").getString(),
                CONTENT_WIDTH);
        y += 25;

        String pressKeyText = Component.translatable("koreanpatch.config.keybindings.press_key",
                (RECORDING_TIMEOUT - elapsed) / 1000).getString();
        guiGraphics.drawString(CLIENT.font, pressKeyText, x, y, WidgetUtils.COLOR_ACCENT, false);
        y += 15;

        String currentKeysDisplay = pressedKeys.isEmpty()
                ? Component.translatable("koreanpatch.config.keybindings.none").getString()
                : new KeyCombination(new ArrayList<>(pressedKeys.values())).getDisplayName();
        String currentKeysText = Component.translatable("koreanpatch.config.keybindings.current_keys",
                currentKeysDisplay).getString();
        guiGraphics.drawString(CLIENT.font, currentKeysText, x, y, WidgetUtils.COLOR_TEXT_SECONDARY, false);
        return y + CLIENT.font.lineHeight;
    }

    private void drawKeyBindingItem(GuiGraphics guiGraphics, int x, int y, String label,
                                     String currentKey, int type, int index, int mouseX, int mouseY) {
        int labelY = y + (BUTTON_HEIGHT - CLIENT.font.lineHeight) / 2;
        guiGraphics.drawString(CLIENT.font, label, x, labelY, WidgetUtils.COLOR_TEXT, false);

        int keyDisplayX = x + 70;
        int keyDisplayWidth = 130;
        WidgetUtils.drawBorderedRect(guiGraphics, keyDisplayX, y, keyDisplayWidth, BUTTON_HEIGHT,
                WidgetUtils.COLOR_WIDGET_BG, WidgetUtils.COLOR_BORDER);
        guiGraphics.drawString(CLIENT.font, currentKey, keyDisplayX + 8, labelY, WidgetUtils.COLOR_TEXT_SECONDARY, false);

        int recordButtonX = x + 210;
        int resetButtonX = x + 290;

        boolean recordHovered = WidgetUtils.isMouseOver(mouseX, mouseY, recordButtonX, y, BUTTON_WIDTH, BUTTON_HEIGHT);
        boolean isRecording = recordingKeyIndex == index && recordingType == type;
        drawButton(guiGraphics, recordButtonX, y, BUTTON_WIDTH, BUTTON_HEIGHT,
                Component.translatable("koreanpatch.config.keybindings.set").getString(), recordHovered, isRecording);

        boolean resetHovered = WidgetUtils.isMouseOver(mouseX, mouseY, resetButtonX, y, BUTTON_WIDTH, BUTTON_HEIGHT);
        drawButton(guiGraphics, resetButtonX, y, BUTTON_WIDTH, BUTTON_HEIGHT,
                Component.translatable("koreanpatch.config.keybindings.remove").getString(), resetHovered, false);
    }

    private void drawButton(GuiGraphics guiGraphics, int x, int y, int width, int height, String text, boolean hovered, boolean active) {
        int bgColor;
        if (active) {
            bgColor = WidgetUtils.COLOR_ACCENT;
        } else if (hovered) {
            bgColor = WidgetUtils.COLOR_BUTTON_HOVER;
        } else {
            bgColor = WidgetUtils.COLOR_WIDGET_BG;
        }
        WidgetUtils.drawBorderedRect(guiGraphics, x, y, width, height, bgColor, WidgetUtils.COLOR_BORDER);
        guiGraphics.drawCenteredString(CLIENT.font, text, x + width / 2, y + (height - CLIENT.font.lineHeight) / 2, WidgetUtils.COLOR_TEXT);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (recordingKeyIndex >= 0) {
            return true;
        }
        if (scroll.handleMouseClicked(mouseX, mouseY, lastContentHeight)) {
            return true;
        }

        int x = (contentWidth - CONTENT_WIDTH) / 2;
        int y = contentStartY + 15 - scroll.getScrollOffset();
        y += SECTION_SPACING;
        int recordButtonX = x + 210;
        int resetButtonX = x + 290;

        // Language toggle key buttons
        for (int i = 0; i < 2; i++) {
            int buttonY = y + i * (ITEM_HEIGHT + ITEM_GAP);
            if (WidgetUtils.isMouseOver(mouseX, mouseY, recordButtonX, buttonY, BUTTON_WIDTH, BUTTON_HEIGHT)) {
                startKeyRecording(0, i);
                return true;
            }
            if (WidgetUtils.isMouseOver(mouseX, mouseY, resetButtonX, buttonY, BUTTON_WIDTH, BUTTON_HEIGHT)) {
                config.removeLangTypeKey(i);
                return true;
            }
        }

        y += 2 * (ITEM_HEIGHT + ITEM_GAP) + SECTION_GAP + SECTION_SPACING;

        // IME toggle key buttons
        for (int i = 0; i < 1; i++) {
            int buttonY = y + i * (ITEM_HEIGHT + ITEM_GAP);
            if (WidgetUtils.isMouseOver(mouseX, mouseY, recordButtonX, buttonY, BUTTON_WIDTH, BUTTON_HEIGHT)) {
                startKeyRecording(1, i);
                return true;
            }
            if (WidgetUtils.isMouseOver(mouseX, mouseY, resetButtonX, buttonY, BUTTON_WIDTH, BUTTON_HEIGHT)) {
                config.removeImeKey(i);
                return true;
            }
        }

        return false;
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
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (recordingKeyIndex >= 0) {
            if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
                cancelKeyRecording();
                return true;
            }
            KeyIdentifier keyId = new KeyIdentifier(keyCode, scanCode);
            pressedKeys.put(keyCode, keyId);
            recordedKeyCombo.clear();
            recordedKeyCombo.addAll(pressedKeys.values());
            return true;
        }
        return false;
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (recordingKeyIndex >= 0) {
            pressedKeys.remove(keyCode);
            if (pressedKeys.isEmpty() && !recordedKeyCombo.isEmpty()) {
                completeKeyRecording();
            }
            return true;
        }
        return false;
    }

    private void startKeyRecording(int type, int index) {
        recordingType = type;
        recordingKeyIndex = index;
        pressedKeys.clear();
        recordedKeyCombo.clear();
        recordingStartTime = System.currentTimeMillis();
    }

    private void cancelKeyRecording() {
        recordingKeyIndex = -1;
        recordingType = -1;
        pressedKeys.clear();
        recordedKeyCombo.clear();
    }

    private void completeKeyRecording() {
        if (!recordedKeyCombo.isEmpty()) {
            List<KeyIdentifier> keyCombo = new ArrayList<>(recordedKeyCombo);
            if (recordingType == 0) {
                if (recordingKeyIndex < config.getLangTypeKeys().size()) {
                    config.getLangTypeKeys().set(recordingKeyIndex, keyCombo);
                } else {
                    config.addLangTypeKey(keyCombo);
                }
            } else if (recordingType == 1) {
                if (recordingKeyIndex < config.getImeKeys().size()) {
                    config.getImeKeys().set(recordingKeyIndex, keyCombo);
                } else {
                    config.addImeKey(keyCombo);
                }
            }
        }
        cancelKeyRecording();
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        return scroll.handleMouseScrolled(scrollY, lastContentHeight);
    }
}
