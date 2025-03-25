package com.hyfata.najoan.koreanpatch.config.indicator;

import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class IndicatorBackground {
    @ConfigEntry.ColorPicker
    int koreanColor = 0x000000;
    @ConfigEntry.ColorPicker
    int enColor = 0x000000;
    @ConfigEntry.ColorPicker
    int imeColor = 0x000000;
    @ConfigEntry.BoundedDiscrete(min=0,max=100)
    int opacity = 50;
}
