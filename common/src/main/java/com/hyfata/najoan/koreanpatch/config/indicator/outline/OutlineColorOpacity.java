package com.hyfata.najoan.koreanpatch.config.indicator.outline;

import com.hyfata.najoan.koreanpatch.config.indicator.ColorOpacityConfig;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class OutlineColorOpacity implements ColorOpacityConfig {
    @ConfigEntry.ColorPicker
    int koreanColor = 0xff0000;
    @ConfigEntry.ColorPicker
    int enColor = 0x00ff00;
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
