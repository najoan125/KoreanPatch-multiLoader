package com.hyfata.najoan.koreanpatch.driver;

import com.hyfata.najoan.koreanpatch.driver.arch.darwin.DarwinController;
import com.hyfata.najoan.koreanpatch.driver.arch.unknown.EmptyController;
import com.hyfata.najoan.koreanpatch.driver.arch.win.WinController;
import org.lwjgl.glfw.GLFW;

public interface InputController {
    void setFocus(final boolean focus);
    void toggleFocus();
    boolean isFocused();

    static InputController newController() {
        int platform = GLFW.glfwGetPlatform();

        if (platform == GLFW.GLFW_PLATFORM_WIN32) {
            return new WinController(); // Windows
        } else if (platform == GLFW.GLFW_PLATFORM_COCOA) {
            return new DarwinController(); // MacOS
        }

        return new EmptyController(); // Other platforms
    }
}
