package com.hyfata.najoan.koreanpatch.keybinding;

import org.lwjgl.glfw.GLFW;

import java.util.Objects;

/**
 * 키 식별자 클래스
 * keyCode 또는 scanCode로 키를 식별합니다.
 * keyCode가 -1이면 scanCode로만 식별합니다.
 */
public class KeyIdentifier {
    private final int keyCode;   // GLFW keyCode (-1이면 무시)
    private final int scanCode;  // scanCode (-1이면 무시)

    public KeyIdentifier(int keyCode, int scanCode) {
        this.keyCode = keyCode;
        this.scanCode = scanCode;
    }

    /**
     * keyCode만으로 생성 (scanCode는 -1)
     */
    public static KeyIdentifier fromKeyCode(int keyCode) {
        return new KeyIdentifier(keyCode, -1);
    }

    /**
     * scanCode만으로 생성 (keyCode는 -1)
     */
    public static KeyIdentifier fromScanCode(int scanCode) {
        return new KeyIdentifier(-1, scanCode);
    }

    public int getKeyCode() {
        return keyCode;
    }

    public int getScanCode() {
        return scanCode;
    }

    /**
     * 키 이벤트와 매칭되는지 확인
     */
    public boolean matches(int eventKeyCode, int eventScanCode) {
        // keyCode가 있으면 keyCode로 매칭
        if (keyCode != -1 && keyCode == eventKeyCode) {
            return true;
        }
        // keyCode가 -1이고 scanCode가 있으면 scanCode로 매칭
        if (keyCode == -1 && scanCode != -1 && scanCode == eventScanCode) {
            return true;
        }
        return false;
    }

    /**
     * 다른 KeyIdentifier와 매칭되는지 확인
     */
    public boolean matches(KeyIdentifier other) {
        return matches(other.keyCode, other.scanCode);
    }

    /**
     * 사람이 읽을 수 있는 키 이름 반환
     */
    public String getDisplayName() {
        if (keyCode != -1) {
            return getKeyName(keyCode);
        } else if (scanCode != -1) {
            return "Scan " + scanCode;
        }
        return "Unknown";
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
            case GLFW.GLFW_KEY_UP -> "Up";
            case GLFW.GLFW_KEY_DOWN -> "Down";
            case GLFW.GLFW_KEY_LEFT -> "Left";
            case GLFW.GLFW_KEY_RIGHT -> "Right";
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
        if (!(o instanceof KeyIdentifier that)) return false;
        // keyCode가 둘 다 있으면 keyCode로 비교
        if (keyCode != -1 && that.keyCode != -1) {
            return keyCode == that.keyCode;
        }
        // 둘 다 scanCode만 있으면 scanCode로 비교
        if (keyCode == -1 && that.keyCode == -1) {
            return scanCode == that.scanCode;
        }
        return false;
    }

    @Override
    public int hashCode() {
        // keyCode가 있으면 keyCode 기반, 아니면 scanCode 기반
        if (keyCode != -1) {
            return Objects.hash(keyCode, 0);
        }
        return Objects.hash(-1, scanCode);
    }

    @Override
    public String toString() {
        return getDisplayName();
    }
}
