package com.hyfata.najoan.koreanpatch.config.indicator;

import com.hyfata.najoan.koreanpatch.config.indicator.outline.Outline;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.*;

public class CategoryIndicator {
    @Tooltip
    boolean showIndicator = true;
    @CollapsibleObject
    Outline outline = new Outline();
    @Tooltip
    @CollapsibleObject
    IndicatorBackgroundColorOpacity background = new IndicatorBackgroundColorOpacity();
    @Tooltip
    @CollapsibleObject
    IndicatorTextColorOpacity text = new IndicatorTextColorOpacity();
    @Tooltip
    @CollapsibleObject
    IndicatorAnimation animation = new IndicatorAnimation();

    public boolean isShowIndicator() {
        return showIndicator;
    }

    public Outline getOutlineSettings() {
        return outline;
    }

    public IndicatorBackgroundColorOpacity getBackgroundSettings() {
        return background;
    }

    public IndicatorTextColorOpacity getTextSettings() {
        return text;
    }

    public IndicatorAnimation getAnimationSettings() {
        return animation;
    }
}
