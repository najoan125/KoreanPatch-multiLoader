package com.hyfata.najoan.koreanpatch.driver.arch.darwin;

import com.hyfata.najoan.koreanpatch.client.Constants;
import com.hyfata.najoan.koreanpatch.config.ConfigManager;
import com.hyfata.najoan.koreanpatch.driver.InputController;
import com.sun.jna.Library;
import com.sun.jna.Native;

public class DarwinController implements InputController {
    private final String GLOBAL_WINDOW_UUID = "minecraft_global_context";
    private boolean focus = false;
    private boolean fakeFocus = false;

    // macOS Core Graphics framework
    // for Caps Lock detection
    public interface CoreGraphics extends Library {
        CoreGraphics INSTANCE = Native.load("CoreGraphics", CoreGraphics.class);
        long CGEventSourceFlagsState(int stateID);
    }
    
    private static final long kCGEventFlagMaskAlphaShift = 0x00010000L; // Caps Lock
    private static final int kCGEventSourceStateHIDSystemState = 0;

    public DarwinController() {
        DarwinHandle.LogInfoCallback info = log -> Constants.LOG.info("[Native|C] {}", log);
        DarwinHandle.LogErrorCallback error = log -> Constants.LOG.error("[Native|C] {}", log);
        DarwinHandle.LogDebugCallback debug = log -> Constants.LOG.debug("[Native|C] {}", log);

        DarwinHandle.INSTANCE.initialize(info, error, debug);
        DarwinHandle.INSTANCE.addInstance(GLOBAL_WINDOW_UUID, null, null, null);
    }

    public boolean isCapsLockOn() {
        try {
            long flags = CoreGraphics.INSTANCE.CGEventSourceFlagsState(kCGEventSourceStateHIDSystemState);
            return (flags & kCGEventFlagMaskAlphaShift) != 0;
        } catch (Exception e) {
            Constants.LOG.debug("Failed to get Caps Lock state: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public void setFocus(boolean focus) {
        boolean alwaysIme = ConfigManager.getInstance().getConfig().getCategoryInput().isAlwaysImeEnabled();

        if (!alwaysIme && !fakeFocus && this.focus == focus) {
            return;
        }

        this.focus = focus;

        if (alwaysIme) {
            if (fakeFocus) return;
            focus = true;
            fakeFocus = true;
        } else if (fakeFocus) {
            fakeFocus = false;
            if (focus) return;
        }

        DarwinHandle.INSTANCE.setIfReceiveEvent(GLOBAL_WINDOW_UUID, (focus ? 1 : 0));
    }

    @Override
    public void toggleFocus() {
        setFocus(!focus);
    }

    @Override
    public boolean isFocused() {
        return focus;
    }
}
