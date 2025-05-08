package com.hyfata.najoan.koreanpatch.process.ime.arch.unknown;

import com.hyfata.najoan.koreanpatch.process.ime.InputController;

public class EmptyController implements InputController {
    private boolean focus = false;

    @Override
    public void setFocus(boolean focus) {
        this.focus = focus;
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
