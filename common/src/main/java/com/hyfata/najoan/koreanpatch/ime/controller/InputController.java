package com.hyfata.najoan.koreanpatch.ime.controller;

import com.hyfata.najoan.koreanpatch.ime.arch.win.WinController;
import com.sun.jna.Platform;
import org.lwjgl.glfw.GLFW;

public interface InputController {
    void setFocus(final boolean focus);
    void toggleFocus();

    static InputController newController() {
        if (Platform.isWindows()) {
            return new WinController();
        }
        return null;
    }
}
