package com.hyfata.najoan.koreanpatch.config.indicator;

import com.hyfata.najoan.koreanpatch.config.indicator.outline.Outline;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.*;

public class Indicator {
    @Tooltip
    boolean showIndicator = true;
    @CollapsibleObject
    Outline outline = new Outline();
}
