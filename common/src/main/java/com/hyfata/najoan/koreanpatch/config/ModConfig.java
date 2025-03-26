package com.hyfata.najoan.koreanpatch.config;

import com.hyfata.najoan.koreanpatch.client.Constants;
import com.hyfata.najoan.koreanpatch.config.indicator.CategoryIndicator;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Category;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.TransitiveObject;

@Config(name = Constants.MOD_ID)
public class ModConfig implements ConfigData {
    @Category("indicatorSettings")
    @TransitiveObject
    CategoryIndicator categoryIndicator = new CategoryIndicator();

    public CategoryIndicator getCategoryIndicator() {
        return categoryIndicator;
    }
}
