package com.hyfata.najoan.koreanpatch.config.indicator;

import com.hyfata.najoan.koreanpatch.util.animation.EasingFunctions;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.*;

public class IndicatorAnimation {
    @Tooltip
    boolean showAnimation = true;

    @Tooltip
    EasingFunctions easingFunction = EasingFunctions.easeOutQuint;

    @Tooltip
    @ConfigEntry.BoundedDiscrete(min=0,max=100)
    int speed = 30;

    public boolean isShowAnimation() {
        return showAnimation;
    }

    public EasingFunctions getEasingFunction() {
        return easingFunction;
    }

    public int getSpeed() {
        return speed;
    }
}
