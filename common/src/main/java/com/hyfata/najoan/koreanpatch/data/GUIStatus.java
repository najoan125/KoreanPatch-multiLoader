package com.hyfata.najoan.koreanpatch.data;

public class GUIStatus {
    private static boolean shouldUseIME = false;
    private static boolean bypassInjection = false;

    public static boolean isShouldUseIME() {
        return shouldUseIME;
    }

    public static void setShouldUseIME(boolean shouldUseIME) {
        GUIStatus.shouldUseIME = shouldUseIME;
    }

    public static boolean isBypassInjection() {
        return bypassInjection;
    }

    public static void setBypassInjection(boolean bypassInjection) {
        GUIStatus.bypassInjection = bypassInjection;
    }
}
