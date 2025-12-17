package com.hyfata.najoan.koreanpatch.keybinding;

import com.hyfata.najoan.koreanpatch.config.ConfigManager;
import com.hyfata.najoan.koreanpatch.config.category.CategoryKeyBindings;
import com.sun.jna.Platform;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
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

    private final Set<Integer> pressedKeys = new HashSet<>();
    private long lastKeyEventTime = 0;

    private KeyBindingManager() {
        initializeDefaults();
    }

    public static KeyBindingManager getInstance() {
        if (instance == null) {
            instance = new KeyBindingManager();
        }
        return instance;
    }

    /**
     * 기본 키바인딩 초기화
     */
    private void initializeDefaults() {
        CategoryKeyBindings keyBindings = ConfigManager.getInstance().getConfig().getCategoryKeyBindings();

        // 저장된 설정이 없거나 비어있으면 기본값 설정
        if (keyBindings.getLangTypeKeys().isEmpty()) {
            List<Integer> defaultLangKey = new ArrayList<>();
            if (Platform.isWindows()) {
                defaultLangKey.add(GLFW.GLFW_KEY_RIGHT_ALT);
            } else if (Platform.isMac()) {
                defaultLangKey.add(GLFW.GLFW_KEY_CAPS_LOCK);
            } else {
                defaultLangKey.add(GLFW.GLFW_KEY_LEFT_CONTROL);
            }
            keyBindings.addLangTypeKey(defaultLangKey);
        }

        if (keyBindings.getImeKeys().isEmpty()) {
            List<Integer> defaultImeKey = new ArrayList<>();
            defaultImeKey.add(GLFW.GLFW_KEY_I);
            keyBindings.addImeKey(defaultImeKey);
        }
    }

    /**
     * 키 눌림 이벤트 처리
     * @param keyCode GLFW 키 코드
     * @param scanCode 스캔 코드 (사용하지 않음)
     * @param action 액션 (GLFW_PRESS, GLFW_RELEASE 등)
     * @param mods 모디파이어 (Ctrl, Shift 등)
     */
    public void onKeyInput(int keyCode, int scanCode, int action, int mods) {
        if (action == GLFW.GLFW_PRESS) {
            pressedKeys.add(keyCode);
            lastKeyEventTime = System.currentTimeMillis();
        } else if (action == GLFW.GLFW_RELEASE) {
            pressedKeys.remove(keyCode);
        }
    }

    /**
     * 현재 눌려있는 키 조합 가져오기
     */
    public Set<Integer> getPressedKeys() {
        return new HashSet<>(pressedKeys);
    }

    /**
     * 한/영 변환 키 조합이 눌렸는지 확인
     */
    public boolean isLangTypeKeyPressed() {
        CategoryKeyBindings keyBindings = ConfigManager.getInstance().getConfig().getCategoryKeyBindings();
        Set<Integer> currentPressedKeys = getPressedKeys();

        for (List<Integer> keyCombo : keyBindings.getLangTypeKeys()) {
            KeyCombination combination = new KeyCombination(keyCombo);
            if (combination.matches(currentPressedKeys)) {
                return true;
            }
        }
        return false;
    }

    /**
     * IME 토글 키 조합이 눌렸는지 확인
     */
    public boolean isImeKeyPressed() {
        CategoryKeyBindings keyBindings = ConfigManager.getInstance().getConfig().getCategoryKeyBindings();
        Set<Integer> currentPressedKeys = getPressedKeys();

        for (List<Integer> keyCombo : keyBindings.getImeKeys()) {
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
    public boolean isKeyComboConflicting(List<Integer> keyCombo) {
        // 한 글자 키만 사용하거나 조합이 없으면 충돌 가능성이 높음
        if (keyCombo.size() == 1) {
            int key = keyCombo.getFirst();
            // 문자/숫자 키는 텍스트 입력에 영향
            return (key >= GLFW.GLFW_KEY_0 && key <= GLFW.GLFW_KEY_9) ||
                   (key >= GLFW.GLFW_KEY_A && key <= GLFW.GLFW_KEY_Z) ||
                   key == GLFW.GLFW_KEY_SPACE ||
                   key == GLFW.GLFW_KEY_ENTER ||
                   key == GLFW.GLFW_KEY_BACKSPACE ||
                   key == GLFW.GLFW_KEY_DELETE;
        }
        return false;
    }

    /**
     * 모든 키바인딩 설정 가져오기
     */
    public CategoryKeyBindings getKeyBindings() {
        return ConfigManager.getInstance().getConfig().getCategoryKeyBindings();
    }

    /**
     * 키바인딩 설정 저장
     */
    public void saveKeyBindings(CategoryKeyBindings keyBindings) {
        ConfigManager.getInstance().saveConfig(ConfigManager.getInstance().getConfig());
        initializeDefaults(); // 새 설정으로 재초기화
    }

    /**
     * 마지막 키 이벤트 시간 가져오기
     */
    public long getLastKeyEventTime() {
        return lastKeyEventTime;
    }

    /**
     * 키 누름 상태 초기화
     */
    public void clearPressedKeys() {
        pressedKeys.clear();
    }
}
