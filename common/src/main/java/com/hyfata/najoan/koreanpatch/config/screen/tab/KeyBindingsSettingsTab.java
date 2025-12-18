package com.hyfata.najoan.koreanpatch.config.screen.tab;

import com.hyfata.najoan.koreanpatch.config.ConfigManager;
import com.hyfata.najoan.koreanpatch.config.category.CategoryKeyBindings;
import com.hyfata.najoan.koreanpatch.config.screen.widget.WidgetUtils;
import com.hyfata.najoan.koreanpatch.keybinding.KeyBindingManager;
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

/**
 * Key bindings settings tab
 * Configure language toggle key and IME toggle key
 */
public class KeyBindingsSettingsTab extends SettingsTab {
    private CategoryKeyBindings config;
    private int scrollOffset = 0;
    private static final int SCROLL_STEP = 15;
    private static final int SECTION_SPACING = 30;
    private static final int ITEM_HEIGHT = 35;
    private static final int BUTTON_WIDTH = 80;
    private static final int BUTTON_HEIGHT = 20;

    private int recordingKeyIndex = -1; // Recording key index (-1 = not recording)
    private int recordingType = -1; // 0 = langTypeKey, 1 = imeKey
    private final Map<Integer, KeyIdentifier> pressedKeys = new HashMap<>(); // keyCode -> KeyIdentifier
    private final List<KeyIdentifier> recordedKeyCombo = new ArrayList<>(); // Final key combination to save
    private long recordingStartTime = 0;
    private static final long RECORDING_TIMEOUT = 5000; // 5 second timeout

    private static final Minecraft client = Minecraft.getInstance();

    @Override
    public Component getTabName() {
        return Component.literal("Key Bindings");
    }

    @Override
    protected void onInit() {
        this.config = ConfigManager.getInstance().getConfig().getCategoryKeyBindings();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int padding = 15;
        int y = contentStartY + padding - scrollOffset;
        int maxWidth = contentWidth - padding * 2;

        // Language toggle key section
        drawSection(guiGraphics, padding, y, "Language Toggle Key (Max 2)", maxWidth);
        y += SECTION_SPACING;

        // List of registered language toggle keys
        List<List<KeyIdentifier>> langTypeKeys = config.getLangTypeKeys();
        for (int i = 0; i < 2; i++) {
            String label = "Key " + (i + 1);
            if (i < langTypeKeys.size()) {
                List<KeyIdentifier> keyCombo = langTypeKeys.get(i);
                String displayName = new KeyCombination(keyCombo).getDisplayName();
                drawKeyBindingItem(guiGraphics, padding, y, label, displayName, 0, i, mouseX, mouseY);
            } else {
                drawKeyBindingItem(guiGraphics, padding, y, label, "(Not Set)", 0, i, mouseX, mouseY);
            }
            y += ITEM_HEIGHT + 5;
        }

        y += 10;

        // IME toggle key section
        drawSection(guiGraphics, padding, y, "IME Toggle Key", maxWidth);
        y += SECTION_SPACING;

        // List of registered IME toggle keys
        List<List<KeyIdentifier>> imeKeys = config.getImeKeys();
        for (int i = 0; i < 1; i++) {
            String label = "Key 1";
            if (i < imeKeys.size()) {
                List<KeyIdentifier> keyCombo = imeKeys.get(i);
                String displayName = new KeyCombination(keyCombo).getDisplayName();
                drawKeyBindingItem(guiGraphics, padding, y, label, displayName, 1, i, mouseX, mouseY);
            } else {
                drawKeyBindingItem(guiGraphics, padding, y, label, "(Not Set)", 1, i, mouseX, mouseY);
            }
            y += ITEM_HEIGHT + 5;
        }

        // Show when recording keys
        if (recordingKeyIndex >= 0) {
            long elapsed = System.currentTimeMillis() - recordingStartTime;
            if (elapsed > RECORDING_TIMEOUT) {
                cancelKeyRecording();
            } else {
                drawSection(guiGraphics, padding, y + 20, "Waiting for Key Input...", maxWidth);
                guiGraphics.drawString(client.font,
                        "Press the desired key combination (" + ((RECORDING_TIMEOUT - elapsed) / 1000) + "s)",
                        padding, y + 50, WidgetUtils.COLOR_ACCENT, false);
                String currentKeysDisplay = pressedKeys.isEmpty() ? "None" :
                        new KeyCombination(new ArrayList<>(pressedKeys.values())).getDisplayName();
                guiGraphics.drawString(client.font,
                        "Currently pressed keys: " + currentKeysDisplay,
                        padding, y + 65, WidgetUtils.COLOR_TEXT_SECONDARY, false);
            }
        }

        // Draw scrollbar
        int scrollbarX = contentWidth - 10;
        int scrollbarY = contentStartY;
        int scrollbarHeight = contentHeight - contentStartY;
        int totalContentHeight = getContentHeight();
        float visibleRatio = (float)(contentHeight - contentStartY) / totalContentHeight;
        int maxScroll = Math.max(0, totalContentHeight - (contentHeight - contentStartY));
        float scrollProgress = maxScroll > 0 ? (float)scrollOffset / maxScroll : 0;

        if (visibleRatio < 1.0f) {
            WidgetUtils.drawScrollbar(guiGraphics, scrollbarX, scrollbarY, scrollbarHeight,
                    scrollProgress, visibleRatio);
        }
    }

    /**
     * Render key binding item
     */
    private void drawKeyBindingItem(GuiGraphics guiGraphics, int x, int y, String label,
                                     String currentKey, int type, int index, int mouseX, int mouseY) {
        // Label
        guiGraphics.drawString(client.font, label, x, y, WidgetUtils.COLOR_TEXT, false);

        // Current key display
        WidgetUtils.drawBorderedRect(guiGraphics, x + 80, y - 2, 150, 20,
                WidgetUtils.COLOR_WIDGET_BG, WidgetUtils.COLOR_BORDER);
        guiGraphics.drawString(client.font, currentKey, x + 90, y,
                WidgetUtils.COLOR_TEXT_SECONDARY, false);

        // Button area
        int recordButtonX = x + 240;
        int resetButtonX = x + 330;

        // Record button
        boolean recordHovered = mouseX >= recordButtonX && mouseX < recordButtonX + BUTTON_WIDTH &&
                                mouseY >= y - 2 && mouseY < y + BUTTON_HEIGHT;
        int recordBgColor = recordingKeyIndex == index && recordingType == type ?
                WidgetUtils.COLOR_ACCENT : (recordHovered ? WidgetUtils.COLOR_WIDGET_HOVER : WidgetUtils.COLOR_WIDGET_BG);
        WidgetUtils.drawBorderedRect(guiGraphics, recordButtonX, y - 2, BUTTON_WIDTH, BUTTON_HEIGHT,
                recordBgColor, WidgetUtils.COLOR_BORDER);
        guiGraphics.drawCenteredString(client.font, "Set",
                recordButtonX + BUTTON_WIDTH / 2, y + 2, WidgetUtils.COLOR_TEXT);

        // Reset button
        boolean resetHovered = mouseX >= resetButtonX && mouseX < resetButtonX + BUTTON_WIDTH &&
                               mouseY >= y - 2 && mouseY < y + BUTTON_HEIGHT;
        int resetBgColor = resetHovered ? WidgetUtils.COLOR_WIDGET_HOVER : WidgetUtils.COLOR_WIDGET_BG;
        WidgetUtils.drawBorderedRect(guiGraphics, resetButtonX, y - 2, BUTTON_WIDTH, BUTTON_HEIGHT,
                resetBgColor, WidgetUtils.COLOR_BORDER);
        guiGraphics.drawCenteredString(client.font, "Remove",
                resetButtonX + BUTTON_WIDTH / 2, y + 2, WidgetUtils.COLOR_TEXT);
    }

    /**
     * Render section title
     */
    private void drawSection(GuiGraphics guiGraphics, int x, int y, String title, int width) {
        guiGraphics.drawString(client.font, title, x, y, WidgetUtils.COLOR_TEXT, false);
        guiGraphics.fill(x, y + 15, x + width, y + 16, WidgetUtils.COLOR_BORDER);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (recordingKeyIndex >= 0) {
            return true; // 녹화 중이면 다른 클릭 무시
        }

        int padding = 15;
        int y = contentStartY + padding - scrollOffset;
        y += SECTION_SPACING; // 섹션 제목 높이 추가
        int recordButtonX = padding + 240;
        int resetButtonX = padding + 330;

        // 한/영 변환키 버튼
        for (int i = 0; i < 2; i++) {
            int buttonY = y + i * (ITEM_HEIGHT + 5);

            // 설정 버튼
            if (mouseX >= recordButtonX && mouseX < recordButtonX + BUTTON_WIDTH &&
                mouseY >= buttonY - 2 && mouseY < buttonY + BUTTON_HEIGHT) {
                startKeyRecording(0, i);
                return true;
            }

            // 제거 버튼
            if (mouseX >= resetButtonX && mouseX < resetButtonX + BUTTON_WIDTH &&
                mouseY >= buttonY - 2 && mouseY < buttonY + BUTTON_HEIGHT) {
                config.removeLangTypeKey(i);
                return true;
            }
        }

        y += 2 * (ITEM_HEIGHT + 5) + 10;
        y += SECTION_SPACING; // IME 섹션 제목 높이 추가

        // IME 토글 키 버튼
        for (int i = 0; i < 1; i++) {
            int buttonY = y + i * (ITEM_HEIGHT + 5);

            // 설정 버튼
            if (mouseX >= recordButtonX && mouseX < recordButtonX + BUTTON_WIDTH &&
                mouseY >= buttonY - 2 && mouseY < buttonY + BUTTON_HEIGHT) {
                startKeyRecording(1, i);
                return true;
            }

            // 제거 버튼
            if (mouseX >= resetButtonX && mouseX < resetButtonX + BUTTON_WIDTH &&
                mouseY >= buttonY - 2 && mouseY < buttonY + BUTTON_HEIGHT) {
                config.removeImeKey(i);
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (recordingKeyIndex >= 0) {
            // Cancel on ESC press
            if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
                cancelKeyRecording();
                return true;
            }
            // Store keyCode and scanCode together
            KeyIdentifier keyId = new KeyIdentifier(keyCode, scanCode);
            pressedKeys.put(keyCode, keyId);
            // Save currently pressed key combination
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

            // Complete when all keys are released
            if (pressedKeys.isEmpty() && !recordedKeyCombo.isEmpty()) {
                completeKeyRecording();
            }
            return true;
        }
        return false;
    }

    /**
     * Start key recording
     */
    private void startKeyRecording(int type, int index) {
        recordingType = type;
        recordingKeyIndex = index;
        pressedKeys.clear();
        recordedKeyCombo.clear();
        recordingStartTime = System.currentTimeMillis();
    }

    /**
     * Cancel key recording
     */
    private void cancelKeyRecording() {
        recordingKeyIndex = -1;
        recordingType = -1;
        pressedKeys.clear();
        recordedKeyCombo.clear();
    }

    /**
     * Complete key recording
     */
    private void completeKeyRecording() {
        if (!recordedKeyCombo.isEmpty()) {
            List<KeyIdentifier> keyCombo = new ArrayList<>(recordedKeyCombo);

            if (recordingType == 0) {
                // Language toggle key
                if (recordingKeyIndex < config.getLangTypeKeys().size()) {
                    config.getLangTypeKeys().set(recordingKeyIndex, keyCombo);
                } else {
                    config.addLangTypeKey(keyCombo);
                }
            } else if (recordingType == 1) {
                // IME toggle key
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
        int maxScroll = getContentHeight() - (contentHeight - contentStartY);
        maxScroll = Math.max(0, maxScroll);
        this.scrollOffset = (int) Math.max(0, Math.min(maxScroll, this.scrollOffset - scrollY * SCROLL_STEP));
        return true;
    }

    /**
     * Calculate total content height
     */
    private int getContentHeight() {
        int padding = 15;
        int height = padding;
        // Language toggle key: section + 2 keys
        height += SECTION_SPACING + (ITEM_HEIGHT + 5) * 2 + 10;
        // IME toggle key: section + 1 key
        height += SECTION_SPACING + (ITEM_HEIGHT + 5);
        // Key recording display area
        height += 100;
        return height;
    }

    @Override
    public void save() {
        KeyBindingManager.getInstance().saveKeyBindings(config);
    }
}
