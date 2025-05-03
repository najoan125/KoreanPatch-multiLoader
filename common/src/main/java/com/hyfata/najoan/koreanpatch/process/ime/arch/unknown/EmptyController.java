package com.hyfata.najoan.koreanpatch.process.ime.arch.unknown;

import com.hyfata.najoan.koreanpatch.process.ime.InputController;

public class EmptyController implements InputController {
    @Override
    public void setFocus(boolean focus) {

    }

    @Override
    public void toggleFocus() {

    }

    @Override
    public boolean isFocused() {
        return false;
    }
}
