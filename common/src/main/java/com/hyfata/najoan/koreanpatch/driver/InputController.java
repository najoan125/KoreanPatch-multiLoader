package com.hyfata.najoan.koreanpatch.driver;

import com.hyfata.najoan.koreanpatch.driver.arch.darwin.DarwinController;
import com.hyfata.najoan.koreanpatch.driver.arch.unknown.EmptyController;
import com.hyfata.najoan.koreanpatch.driver.arch.win.WinController;
import com.sun.jna.Platform;

public interface InputController {
    void setFocus(final boolean focus);
    void toggleFocus();
    boolean isFocused();

    static InputController newController() {
        if (Platform.isWindows()) {
            return new WinController(); // Windows
        } else if (Platform.isMac()) {
            return new DarwinController(); // MacOS
        }

        return new EmptyController(); // Other platforms
    }
}
