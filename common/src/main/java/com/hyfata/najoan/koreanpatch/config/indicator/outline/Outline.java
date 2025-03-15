package com.hyfata.najoan.koreanpatch.config.indicator.outline;

import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.*;

public class Outline {
    @Tooltip
    boolean showOutline = true;
    boolean rounded = true;
    @Tooltip
    @CollapsibleObject
    OutlineColorOpacity colorOpacity = new OutlineColorOpacity();
}
