package com.hyfata.najoan.koreanpatch.arch.win;

import com.hyfata.najoan.koreanpatch.client.Constants;
import com.hyfata.najoan.koreanpatch.client.KoreanPatchClient;
import com.hyfata.najoan.koreanpatch.plugin.InputController;
import net.minecraft.client.Minecraft;

public class WinController implements InputController {
    private boolean focus = false;

    @Override
    public void setFocus(boolean focus) {
        if (this.focus == focus) {
            return;
        }
        this.focus = focus;
        KoreanPatchClient.IME = focus;
        WinHandle.INSTANCE.set_focus(focus ? 1 : 0);
    }

    @Override
    public void toggleFocus() {
        setFocus(!focus);
    }

    WinHandle.PreeditCallback pc = (str, cursor, length) -> {};
    WinHandle.DoneCallback dc = (str) -> {};
    WinHandle.RectCallback rc = ret -> 1;

    public WinController() {
        WinHandle.INSTANCE.initialize(org.lwjgl.glfw.GLFWNativeWin32.glfwGetWin32Window(Minecraft.getInstance().getWindow().getWindow()), pc, dc,rc, (log) -> Constants.LOG.info("[Native|C] {}", log), (log) -> Constants.LOG.error("[Native|C] {}", log), (log) -> Constants.LOG.debug("[Native|C] {}", log));
    }
}
