package com.hyfata.najoan.koreanpatch.ime.controller;

import com.hyfata.najoan.koreanpatch.ime.arch.win.WinController;
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
