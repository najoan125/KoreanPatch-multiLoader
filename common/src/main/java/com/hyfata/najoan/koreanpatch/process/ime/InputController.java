package com.hyfata.najoan.koreanpatch.process.ime;

import com.hyfata.najoan.koreanpatch.process.ime.arch.unknown.EmptyController;
import com.hyfata.najoan.koreanpatch.process.ime.arch.win.WinController;
import org.lwjgl.glfw.GLFW;

public interface InputController {
    void setFocus(final boolean focus);
    void toggleFocus();
    boolean isFocused();

    static InputController newController() {
        return switch (GLFW.glfwGetPlatform()) {
            case GLFW.GLFW_PLATFORM_WIN32 -> new WinController();
            default -> new EmptyController();
        };
    }
}
