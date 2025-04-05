package com.hyfata.najoan.koreanpatch.data.config.category.indicator;

import com.hyfata.najoan.koreanpatch.data.provider.EasingFunctions;

public class IndicatorAnimationConfig {
    boolean showAnimation = true;
    EasingFunctions easingFunction = EasingFunctions.easeOutQuint;
    int speed = 30;

    public IndicatorAnimationConfig() {}

    public IndicatorAnimationConfig(boolean showAnimation, EasingFunctions easingFunction, int speed) {
        this.showAnimation = showAnimation;
        this.easingFunction = easingFunction;
        this.speed = speed;
    }

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
