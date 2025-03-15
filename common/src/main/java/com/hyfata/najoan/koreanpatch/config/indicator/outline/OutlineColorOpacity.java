package com.hyfata.najoan.koreanpatch.config.indicator.outline;

import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class OutlineColorOpacity {
    @ConfigEntry.ColorPicker
    int koreanColor = 0xff0000;
    @ConfigEntry.ColorPicker
    int enColor = 0x00ff00;
    @ConfigEntry.ColorPicker
    int imeColor = 0xffffff;
    @ConfigEntry.BoundedDiscrete(min=0,max=100)
    int opacity = 100;
}
