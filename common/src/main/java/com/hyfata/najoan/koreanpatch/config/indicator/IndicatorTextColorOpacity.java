package com.hyfata.najoan.koreanpatch.config.indicator;

import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class IndicatorTextColorOpacity implements ColorOpacityConfig {
    @ConfigEntry.ColorPicker
    int koreanColor = 0xffffff;
    @ConfigEntry.ColorPicker
    int enColor = 0xffffff;
    @ConfigEntry.ColorPicker
    int imeColor = 0xffffff;
    @ConfigEntry.BoundedDiscrete(min=0,max=100)
    int opacity = 100;

    @Override
    public int getKoreanColor() {
        return koreanColor;
    }

    @Override
    public int getEnColor() {
        return enColor;
    }

    @Override
    public int getImeColor() {
        return imeColor;
    }

    @Override
    public int getOpacity() {
        return opacity;
    }
}
