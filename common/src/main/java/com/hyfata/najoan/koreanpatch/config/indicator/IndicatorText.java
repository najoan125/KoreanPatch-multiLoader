package com.hyfata.najoan.koreanpatch.config.indicator;

import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class IndicatorText {
    @ConfigEntry.ColorPicker
    int koreanColor = 0xffffff;
    @ConfigEntry.ColorPicker
    int enColor = 0xffffff;
    @ConfigEntry.ColorPicker
    int imeColor = 0xffffff;
    @ConfigEntry.BoundedDiscrete(min=0,max=100)
    int opacity = 100;
}
