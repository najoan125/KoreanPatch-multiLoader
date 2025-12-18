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
    private static final int SECTION_SPACING = 25;  // Space after section title (title height + gap to first item)
    private static final int SECTION_GAP = 15;      // Gap between sections (after last item of previous section)
    private static final int ITEM_HEIGHT = 30;
    private static final int CONTENT_WIDTH = 380;
    private static final int BUTTON_WIDTH = 70;
    private static final int BUTTON_HEIGHT = 20;

    private int recordingKeyIndex = -1; // Recording key index (-1 = not recording)
    private int recordingType = -1; // 0 = langTypeKey, 1 = imeKey
    private final Map<Integer, KeyIdentifier> pressedKeys = new HashMap<>(); // keyCode -> KeyIdentifier
    private final List<KeyIdentifier> recordedKeyCombo = new ArrayList<>(); // Final key combination to save
    private long recordingStartTime = 0;
    private static final long RECORDING_TIMEOUT = 5000; // 5 second timeout

    // Scrollbar drag state
    private boolean isDraggingScrollbar = false;
    private int scrollbarX, scrollbarY, scrollbarHeight;

    private static final Minecraft client = Minecraft.getInstance();

    @Override
    public Component getTabName() {
        return Component.translatable("koreanpatch.config.tab.keybindings");
    }

    @Override
    protected void onInit() {
        this.config = ConfigManager.getInstance().getConfig().getCategoryKeyBindings();
    }

    @Override
    public boolean isRecordingKey() {
        return recordingKeyIndex >= 0;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int padding = 15;
        int contentX = (contentWidth - CONTENT_WIDTH) / 2; // Center content
        int y = contentStartY + padding - scrollOffset;

        // Language toggle key section
        drawSection(guiGraphics, contentX, y, Component.translatable("koreanpatch.config.keybindings.langtype").getString(), CONTENT_WIDTH);
        y += SECTION_SPACING;

        // List of registered language toggle keys
        List<List<KeyIdentifier>> langTypeKeys = config.getLangTypeKeys();
        for (int i = 0; i < 2; i++) {
            String label = Component.translatable("koreanpatch.config.keybindings.key", i + 1).getString();
            if (i < langTypeKeys.size()) {
                List<KeyIdentifier> keyCombo = langTypeKeys.get(i);
                String displayName = new KeyCombination(keyCombo).getDisplayName();
                drawKeyBindingItem(guiGraphics, contentX, y, label, displayName, 0, i, mouseX, mouseY);
            } else {
                drawKeyBindingItem(guiGraphics, contentX, y, label,
                        Component.translatable("koreanpatch.config.keybindings.notset").getString(), 0, i, mouseX, mouseY);
            }
            y += ITEM_HEIGHT + 5;
        }

        y += SECTION_GAP;

        // IME toggle key section
        drawSection(guiGraphics, contentX, y, Component.translatable("koreanpatch.config.keybindings.ime").getString(), CONTENT_WIDTH);
        y += SECTION_SPACING;

        // List of registered IME toggle keys
        List<List<KeyIdentifier>> imeKeys = config.getImeKeys();
        for (int i = 0; i < 1; i++) {
            String label = Component.translatable("koreanpatch.config.keybindings.key", 1).getString();
            if (i < imeKeys.size()) {
                List<KeyIdentifier> keyCombo = imeKeys.get(i);
                String displayName = new KeyCombination(keyCombo).getDisplayName();
                drawKeyBindingItem(guiGraphics, contentX, y, label, displayName, 1, i, mouseX, mouseY);
            } else {
                drawKeyBindingItem(guiGraphics, contentX, y, label,
                        Component.translatable("koreanpatch.config.keybindings.notset").getString(), 1, i, mouseX, mouseY);
            }
            y += ITEM_HEIGHT + 5;
        }

        // Show when recording keys
        if (recordingKeyIndex >= 0) {
            long elapsed = System.currentTimeMillis() - recordingStartTime;
            if (elapsed > RECORDING_TIMEOUT) {
                cancelKeyRecording();
            } else {
                y += 20;
                drawSection(guiGraphics, contentX, y, Component.translatable("koreanpatch.config.keybindings.waiting").getString(), CONTENT_WIDTH);
                y += 25;

                String pressKeyText = Component.translatable("koreanpatch.config.keybindings.press_key", (RECORDING_TIMEOUT - elapsed) / 1000).getString();
                guiGraphics.drawString(client.font, pressKeyText, contentX, y, WidgetUtils.COLOR_ACCENT, false);
                y += 15;

                String currentKeysDisplay = pressedKeys.isEmpty() ?
                        Component.translatable("koreanpatch.config.keybindings.none").getString() :
                        new KeyCombination(new ArrayList<>(pressedKeys.values())).getDisplayName();
                String currentKeysText = Component.translatable("koreanpatch.config.keybindings.current_keys", currentKeysDisplay).getString();
                guiGraphics.drawString(client.font, currentKeysText, contentX, y, WidgetUtils.COLOR_TEXT_SECONDARY, false);
            }
        }

        // Draw scrollbar
        scrollbarX = contentWidth - 10;
        scrollbarY = contentStartY;
        scrollbarHeight = contentHeight - contentStartY;
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
        // Label with vertical centering
        int labelY = y + (BUTTON_HEIGHT - client.font.lineHeight) / 2;
        guiGraphics.drawString(client.font, label, x, labelY, WidgetUtils.COLOR_TEXT, false);

        // Current key display
        int keyDisplayX = x + 70;
        int keyDisplayWidth = 130;
        WidgetUtils.drawBorderedRect(guiGraphics, keyDisplayX, y, keyDisplayWidth, BUTTON_HEIGHT, WidgetUtils.COLOR_WIDGET_BG, WidgetUtils.COLOR_BORDER);
        guiGraphics.drawString(client.font, currentKey, keyDisplayX + 8, labelY, WidgetUtils.COLOR_TEXT_SECONDARY, false);

        // Button area
        int recordButtonX = x + 210;
        int resetButtonX = x + 290;

        // Record button
        boolean recordHovered = WidgetUtils.isMouseOver(mouseX, mouseY, recordButtonX, y, BUTTON_WIDTH, BUTTON_HEIGHT);
        boolean isRecording = recordingKeyIndex == index && recordingType == type;
        drawButton(guiGraphics, recordButtonX, y, BUTTON_WIDTH, BUTTON_HEIGHT,
                Component.translatable("koreanpatch.config.keybindings.set").getString(), recordHovered, isRecording);

        // Reset button
        boolean resetHovered = WidgetUtils.isMouseOver(mouseX, mouseY, resetButtonX, y, BUTTON_WIDTH, BUTTON_HEIGHT);
        drawButton(guiGraphics, resetButtonX, y, BUTTON_WIDTH, BUTTON_HEIGHT,
                Component.translatable("koreanpatch.config.keybindings.remove").getString(), resetHovered, false);
    }

    /**
     * Draw a button with sharp corners
     */
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
        guiGraphics.drawCenteredString(client.font, text, x + width / 2, y + (height - client.font.lineHeight) / 2, WidgetUtils.COLOR_TEXT);
    }

    /**
     * Render section title
     */
    private void drawSection(GuiGraphics guiGraphics, int x, int y, String title, int width) {
        WidgetUtils.drawSectionTitle(guiGraphics, x, y, title);
        guiGraphics.fill(x, y + 14, x + width, y + 15, WidgetUtils.COLOR_BORDER);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (recordingKeyIndex >= 0) {
            return true; // Ignore other clicks while recording
        }

        // Check scrollbar click
        int totalContentHeight = getContentHeight();
        float visibleRatio = (float)(contentHeight - contentStartY) / totalContentHeight;
        if (visibleRatio < 1.0f) {
            int maxScroll = Math.max(0, totalContentHeight - (contentHeight - contentStartY));
            float scrollProgress = maxScroll > 0 ? (float)scrollOffset / maxScroll : 0;
            int[] thumbBounds = WidgetUtils.getScrollbarThumbBounds(scrollbarX, scrollbarY, scrollbarHeight, scrollProgress, visibleRatio);

            if (WidgetUtils.isMouseOver(mouseX, mouseY, thumbBounds[0], thumbBounds[1], thumbBounds[2], thumbBounds[3])) {
                isDraggingScrollbar = true;
                return true;
            }
        }

        int padding = 15;
        int contentX = (contentWidth - CONTENT_WIDTH) / 2;
        int y = contentStartY + padding - scrollOffset;
        y += SECTION_SPACING; // Section title height
        int recordButtonX = contentX + 210;
        int resetButtonX = contentX + 290;

        // Language toggle key buttons
        for (int i = 0; i < 2; i++) {
            int buttonY = y + i * (ITEM_HEIGHT + 5);

            // Set button
            if (WidgetUtils.isMouseOver(mouseX, mouseY, recordButtonX, buttonY, BUTTON_WIDTH, BUTTON_HEIGHT)) {
                startKeyRecording(0, i);
                return true;
            }

            // Remove button
            if (WidgetUtils.isMouseOver(mouseX, mouseY, resetButtonX, buttonY, BUTTON_WIDTH, BUTTON_HEIGHT)) {
                config.removeLangTypeKey(i);
                return true;
            }
        }

        y += 2 * (ITEM_HEIGHT + 5) + SECTION_GAP;
        y += SECTION_SPACING; // IME section title height

        // IME toggle key buttons
        for (int i = 0; i < 1; i++) {
            int buttonY = y + i * (ITEM_HEIGHT + 5);

            // Set button
            if (WidgetUtils.isMouseOver(mouseX, mouseY, recordButtonX, buttonY, BUTTON_WIDTH, BUTTON_HEIGHT)) {
                startKeyRecording(1, i);
                return true;
            }

            // Remove button
            if (WidgetUtils.isMouseOver(mouseX, mouseY, resetButtonX, buttonY, BUTTON_WIDTH, BUTTON_HEIGHT)) {
                config.removeImeKey(i);
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (isDraggingScrollbar) {
            isDraggingScrollbar = false;
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (isDraggingScrollbar) {
            int totalContentHeight = getContentHeight();
            int maxScroll = Math.max(0, totalContentHeight - (contentHeight - contentStartY));
            float visibleRatio = (float)(contentHeight - contentStartY) / totalContentHeight;
            int thumbHeight = Math.max(20, (int)(scrollbarHeight * visibleRatio));

            float progress = (float)(mouseY - scrollbarY - thumbHeight / 2) / (scrollbarHeight - thumbHeight);
            progress = Math.max(0, Math.min(1, progress));
            scrollOffset = (int)(progress * maxScroll);
            return true;
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
        int height = 15;
        // Language toggle key: section + 2 keys + section gap
        height += SECTION_SPACING + (ITEM_HEIGHT + 5) * 2 + SECTION_GAP;
        // IME toggle key: section + 1 key (no gap after last section)
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
