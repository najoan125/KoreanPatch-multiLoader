package com.hyfata.najoan.koreanpatch.process.ime;

import com.hyfata.najoan.koreanpatch.process.ime.arch.unknown.EmptyController;
import com.hyfata.najoan.koreanpatch.process.ime.arch.win.WinController;
import com.sun.jna.Platform;
import org.lwjgl.glfw.GLFW;

public interface InputController {
    void setFocus(final boolean focus);
    void toggleFocus();
    boolean isFocused();

    static InputController newController() {
        if (Platform.isWindows()) {
            return new WinController();
        }
        return new EmptyController();
    }
}
