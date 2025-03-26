package com.hyfata.najoan.koreanpatch.config.indicator.outline;

import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.*;

public class Outline {
    @Tooltip
    boolean showOutline = true;
    boolean rounded = true;
    @Tooltip
    @CollapsibleObject
    OutlineColorOpacity colorOpacity = new OutlineColorOpacity();

    public boolean isShowOutline() {
        return showOutline;
    }

    public boolean isRounded() {
        return rounded;
    }

    public OutlineColorOpacity getColorOpacitySettings() {
        return colorOpacity;
    }
}
