package com.hyfata.najoan.koreanpatch.plugin;

import com.hyfata.najoan.koreanpatch.arch.win.WinController;
import org.lwjgl.glfw.GLFW;

public interface InputController {
    void setFocus(final boolean focus);
    void toggleFocus();

    static InputController newController() {
        return switch (GLFW.glfwGetPlatform()) {
            case GLFW.GLFW_PLATFORM_WIN32 -> new WinController();
            default -> null;
        };
    }
}
