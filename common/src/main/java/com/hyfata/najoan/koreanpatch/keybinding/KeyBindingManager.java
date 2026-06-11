package com.hyfata.najoan.koreanpatch.keybinding;

import com.hyfata.najoan.koreanpatch.config.ConfigManager;
import com.hyfata.najoan.koreanpatch.config.category.CategoryKeyBindings;
import org.lwjgl.glfw.GLFW;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 키바인딩 관리 시스템
 * 설정 파일에서 로드한 키 조합으로 동작합니다.
 * Minecraft의 KeyMapping과 독립적으로 작동하여 다른 키와 겹쳐도 정상 작동합니다.
 */
public class KeyBindingManager {
    private static KeyBindingManager instance;

    private final Set<KeyIdentifier> pressedKeys = new HashSet<>();
    private long lastKeyEventTime = 0;

    private KeyBindingManager() {
    }

    public static KeyBindingManager getInstance() {
        if (instance == null) {
            instance = new KeyBindingManager();
        }
        return instance;
    }

    public void onKeyInput(int keyCode, int scanCode, int action, int mods) {
        KeyIdentifier key = new KeyIdentifier(keyCode, scanCode);
        if (action == GLFW.GLFW_PRESS) {
            pressedKeys.add(key);
            lastKeyEventTime = System.currentTimeMillis();
        } else if (action == GLFW.GLFW_RELEASE) {
            pressedKeys.removeIf(k -> k.matches(keyCode, scanCode));
        }
    }

    public Set<KeyIdentifier> getPressedKeys() {
        return new HashSet<>(pressedKeys);
    }

    public boolean isLangTypeKeyPressed() {
        CategoryKeyBindings keyBindings = ConfigManager.getInstance().getConfig().getCategoryKeyBindings();
        Set<KeyIdentifier> currentPressedKeys = getPressedKeys();

        for (List<KeyIdentifier> keyCombo : keyBindings.getLangTypeKeys()) {
            KeyCombination combination = new KeyCombination(keyCombo);
            if (combination.matches(currentPressedKeys)) {
                return true;
            }
        }
        return false;
    }

    public boolean isImeKeyPressed() {
        CategoryKeyBindings keyBindings = ConfigManager.getInstance().getConfig().getCategoryKeyBindings();
        Set<KeyIdentifier> currentPressedKeys = getPressedKeys();

        for (List<KeyIdentifier> keyCombo : keyBindings.getImeKeys()) {
            KeyCombination combination = new KeyCombination(keyCombo);
            if (combination.matches(currentPressedKeys)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 키 조합이 텍스트 입력과 충돌하는지 확인
     * (입력 필드에서 사용되는 기본 키는 제외)
     */
    public boolean isKeyComboConflicting(List<KeyIdentifier> keyCombo) {
        if (keyCombo.size() == 1) {
            int key = keyCombo.getFirst().getKeyCode();
            if (key == -1) return false;
            return (key >= GLFW.GLFW_KEY_0 && key <= GLFW.GLFW_KEY_9) ||
                   (key >= GLFW.GLFW_KEY_A && key <= GLFW.GLFW_KEY_Z) ||
                   key == GLFW.GLFW_KEY_SPACE ||
                   key == GLFW.GLFW_KEY_ENTER ||
                   key == GLFW.GLFW_KEY_BACKSPACE ||
                   key == GLFW.GLFW_KEY_DELETE;
        }
        return false;
    }

    public long getLastKeyEventTime() {
        return lastKeyEventTime;
    }

    public void clearPressedKeys() {
        pressedKeys.clear();
    }
}
