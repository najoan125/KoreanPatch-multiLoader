package com.hyfata.najoan.koreanpatch.driver.arch.win;

import com.hyfata.najoan.koreanpatch.client.Constants;
import com.hyfata.najoan.koreanpatch.config.ConfigManager;
import com.hyfata.najoan.koreanpatch.driver.InputController;

import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFWNativeWin32;

public class WinController implements InputController {
    private boolean focus = false;
    private boolean fakeFocus = false;

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

        WinHandle.INSTANCE.set_focus(focus ? 1 : 0);
    }

    @Override
    public void toggleFocus() {
        setFocus(!focus);
    }

    @Override
    public boolean isFocused() {
        return focus;
    }

    public WinController() {
        long window = Minecraft.getInstance().getWindow().handle();
        WinHandle.LogInfoCallback info = log -> Constants.LOG.info("[Native|C] {}", log);
        WinHandle.LogErrorCallback error = log -> Constants.LOG.error("[Native|C] {}", log);
        WinHandle.LogDebugCallback debug = log -> Constants.LOG.debug("[Native|C] {}", log);

        WinHandle.INSTANCE.initialize(GLFWNativeWin32.glfwGetWin32Window(window), info, error, debug);
    }
}
