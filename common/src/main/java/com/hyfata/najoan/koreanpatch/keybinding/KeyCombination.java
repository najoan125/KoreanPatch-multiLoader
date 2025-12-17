package com.hyfata.najoan.koreanpatch.keybinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.lwjgl.glfw.GLFW;

/**
 * 키 조합을 나타내는 클래스
 * 여러 키가 동시에 눌려있는 상태를 표현합니다.
 */
public class KeyCombination {
    private final List<Integer> keys;
    private final Set<Integer> keySet;

    public KeyCombination(List<Integer> keys) {
        this.keys = new ArrayList<>(keys);
        Collections.sort(this.keys); // 일관된 비교를 위해 정렬
        this.keySet = new HashSet<>(this.keys);
    }

    public KeyCombination(Integer... keys) {
        this(new ArrayList<>(List.of(keys)));
    }

    /**
     * 이 조합에 속한 모든 키를 가져옵니다.
     */
    public List<Integer> getKeys() {
        return new ArrayList<>(keys);
    }

    /**
     * 이 조합의 키 개수
     */
    public int size() {
        return keys.size();
    }

    /**
     * 특정 키가 포함되어 있는지 확인
     */
    public boolean contains(int key) {
        return keySet.contains(key);
    }

    /**
     * 현재 눌려있는 모든 키가 이 조합에 정확히 일치하는지 확인
     */
    public boolean matches(Set<Integer> pressedKeys) {
        return this.keySet.equals(pressedKeys);
    }

    /**
     * 모든 키가 눌려있는지 확인 (추가 키 눌림 무시)
     */
    public boolean isPartiallyPressed(Set<Integer> pressedKeys) {
        return pressedKeys.containsAll(this.keySet);
    }

    /**
     * 사람이 읽을 수 있는 키 조합 문자열 반환
     */
    public String getDisplayName() {
        List<String> keyNames = new ArrayList<>();
        for (int key : keys) {
            keyNames.add(getKeyName(key));
        }
        return String.join(" + ", keyNames);
    }

    /**
     * GLFW 키 코드를 사람이 읽을 수 있는 이름으로 변환
     */
    public static String getKeyName(int glfwKeyCode) {
        return switch (glfwKeyCode) {
            case GLFW.GLFW_KEY_LEFT_CONTROL, GLFW.GLFW_KEY_RIGHT_CONTROL -> "Ctrl";
            case GLFW.GLFW_KEY_LEFT_SHIFT, GLFW.GLFW_KEY_RIGHT_SHIFT -> "Shift";
            case GLFW.GLFW_KEY_LEFT_ALT, GLFW.GLFW_KEY_RIGHT_ALT -> "Alt";
            case GLFW.GLFW_KEY_LEFT_SUPER, GLFW.GLFW_KEY_RIGHT_SUPER -> "Super";
            case GLFW.GLFW_KEY_ESCAPE -> "Esc";
            case GLFW.GLFW_KEY_TAB -> "Tab";
            case GLFW.GLFW_KEY_BACKSPACE -> "Backspace";
            case GLFW.GLFW_KEY_ENTER -> "Enter";
            case GLFW.GLFW_KEY_SPACE -> "Space";
            case GLFW.GLFW_KEY_CAPS_LOCK -> "Caps Lock";
            case GLFW.GLFW_KEY_F1 -> "F1";
            case GLFW.GLFW_KEY_F2 -> "F2";
            case GLFW.GLFW_KEY_F3 -> "F3";
            case GLFW.GLFW_KEY_F4 -> "F4";
            case GLFW.GLFW_KEY_F5 -> "F5";
            case GLFW.GLFW_KEY_F6 -> "F6";
            case GLFW.GLFW_KEY_F7 -> "F7";
            case GLFW.GLFW_KEY_F8 -> "F8";
            case GLFW.GLFW_KEY_F9 -> "F9";
            case GLFW.GLFW_KEY_F10 -> "F10";
            case GLFW.GLFW_KEY_F11 -> "F11";
            case GLFW.GLFW_KEY_F12 -> "F12";
            case GLFW.GLFW_KEY_UP -> "↑";
            case GLFW.GLFW_KEY_DOWN -> "↓";
            case GLFW.GLFW_KEY_LEFT -> "←";
            case GLFW.GLFW_KEY_RIGHT -> "→";
            case GLFW.GLFW_KEY_A -> "A";
            case GLFW.GLFW_KEY_B -> "B";
            case GLFW.GLFW_KEY_C -> "C";
            case GLFW.GLFW_KEY_D -> "D";
            case GLFW.GLFW_KEY_E -> "E";
            case GLFW.GLFW_KEY_F -> "F";
            case GLFW.GLFW_KEY_G -> "G";
            case GLFW.GLFW_KEY_H -> "H";
            case GLFW.GLFW_KEY_I -> "I";
            case GLFW.GLFW_KEY_J -> "J";
            case GLFW.GLFW_KEY_K -> "K";
            case GLFW.GLFW_KEY_L -> "L";
            case GLFW.GLFW_KEY_M -> "M";
            case GLFW.GLFW_KEY_N -> "N";
            case GLFW.GLFW_KEY_O -> "O";
            case GLFW.GLFW_KEY_P -> "P";
            case GLFW.GLFW_KEY_Q -> "Q";
            case GLFW.GLFW_KEY_R -> "R";
            case GLFW.GLFW_KEY_S -> "S";
            case GLFW.GLFW_KEY_T -> "T";
            case GLFW.GLFW_KEY_U -> "U";
            case GLFW.GLFW_KEY_V -> "V";
            case GLFW.GLFW_KEY_W -> "W";
            case GLFW.GLFW_KEY_X -> "X";
            case GLFW.GLFW_KEY_Y -> "Y";
            case GLFW.GLFW_KEY_Z -> "Z";
            case GLFW.GLFW_KEY_0 -> "0";
            case GLFW.GLFW_KEY_1 -> "1";
            case GLFW.GLFW_KEY_2 -> "2";
            case GLFW.GLFW_KEY_3 -> "3";
            case GLFW.GLFW_KEY_4 -> "4";
            case GLFW.GLFW_KEY_5 -> "5";
            case GLFW.GLFW_KEY_6 -> "6";
            case GLFW.GLFW_KEY_7 -> "7";
            case GLFW.GLFW_KEY_8 -> "8";
            case GLFW.GLFW_KEY_9 -> "9";
            default -> "Key " + glfwKeyCode;
        };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof KeyCombination that)) return false;
        return keys.equals(that.keys);
    }

    @Override
    public int hashCode() {
        return keys.hashCode();
    }

    @Override
    public String toString() {
        return getDisplayName();
    }
}
