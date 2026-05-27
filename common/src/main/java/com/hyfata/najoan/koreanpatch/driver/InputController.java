package com.hyfata.najoan.koreanpatch.driver;

import com.hyfata.najoan.koreanpatch.driver.arch.darwin.DarwinController;
import com.hyfata.najoan.koreanpatch.driver.arch.unknown.EmptyController;
import com.hyfata.najoan.koreanpatch.driver.arch.win.WinController;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.Platform;
import org.lwjgl.system.Platform.Architecture;

public interface InputController {
    void setFocus(final boolean focus);
    void toggleFocus();
    boolean isFocused();

    static InputController newController() {
        int platform = GLFW.glfwGetPlatform();
        Architecture arch = Platform.getArchitecture();

        if (platform == GLFW.GLFW_PLATFORM_WIN32 && 
            (arch == Architecture.X64 || arch == Architecture.ARM64)) { // Windows
            return new WinController();
        } else if (platform == GLFW.GLFW_PLATFORM_COCOA && 
            arch == Architecture.ARM64) { // MacOS
            return new DarwinController();
        }

        return new EmptyController(); // Other platforms
    }
}
