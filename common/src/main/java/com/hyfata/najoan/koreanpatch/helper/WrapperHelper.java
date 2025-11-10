package com.hyfata.najoan.koreanpatch.helper;

public class WrapperHelper {
    private static boolean isPacketEnabled = true;

    public static boolean isPacketEnabled() {
        return isPacketEnabled;
    }

    public static void setPacketEnabled(boolean isPacketEnabled) {
        WrapperHelper.isPacketEnabled = isPacketEnabled;
    }
}
