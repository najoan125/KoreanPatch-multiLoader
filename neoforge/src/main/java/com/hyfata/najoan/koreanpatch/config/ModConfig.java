package com.hyfata.najoan.koreanpatch.config;

import com.hyfata.najoan.koreanpatch.client.Constants;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Category;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.Tooltip;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.TransitiveObject;

@Config(name = Constants.MOD_ID)
public class ModConfig implements ConfigData {
    @Category("indicatorSettings")
    @TransitiveObject
    public IndicatorSettings indicatorSettings = new IndicatorSettings();

    public static class IndicatorSettings {
        @Tooltip
        private boolean showIndicator = true;
        @Tooltip
        private boolean showOutline = true;
    }
}
