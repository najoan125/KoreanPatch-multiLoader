package com.hyfata.najoan.koreanpatch.process.ime.arch.win;

import com.hyfata.najoan.koreanpatch.client.Constants;
import com.hyfata.najoan.koreanpatch.data.ConfigManager;
import com.hyfata.najoan.koreanpatch.process.ime.InputController;
import net.minecraft.client.Minecraft;

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
            if (this.focus) return;
            focus = true;
            fakeFocus = true;
        } else {
            fakeFocus = false;
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
        WinHandle.PreeditCallback pc = (str, cursor, length) -> {};
        WinHandle.DoneCallback dc = (str) -> {};
        WinHandle.RectCallback rc = ret -> 1;

        WinHandle.INSTANCE.initialize(org.lwjgl.glfw.GLFWNativeWin32.glfwGetWin32Window(Minecraft.getInstance().getWindow().getWindow()), pc, dc,rc, (log) -> Constants.LOG.info("[Native|C] {}", log), (log) -> Constants.LOG.error("[Native|C] {}", log), (log) -> Constants.LOG.debug("[Native|C] {}", log));
    }
}
