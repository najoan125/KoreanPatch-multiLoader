package com.hyfata.najoan.koreanpatch.client;

public class GUIStatus {
    private static final GUIStatus instance = new GUIStatus();
    public static GUIStatus getInstance() {
        return instance;
    }

    private boolean shouldUseIME = false;
    private boolean bypassInjection = false;

    public boolean isShouldUseIME() {
        return shouldUseIME;
    }

    public void setShouldUseIME(boolean shouldUseIME) {
        this.shouldUseIME = shouldUseIME;
    }

    public boolean isBypassInjection() {
        return bypassInjection;
    }

    public void setBypassInjection(boolean bypassInjection) {
        this.bypassInjection = bypassInjection;
    }
}
