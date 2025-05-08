package com.hyfata.najoan.koreanpatch.data.config.category.indicator;

import com.hyfata.najoan.koreanpatch.data.gson.JsonComment;
import com.hyfata.najoan.koreanpatch.data.provider.EasingFunctions;

public class IndicatorAnimationConfig {
    private boolean showAnimation = true;

    @JsonComment(value = "Easing function: ", enums = true)
    private EasingFunctions easingFunction = EasingFunctions.EASE_OUT_QUINT;

    private int speed = 30;

    public boolean isShowAnimation() {
        return showAnimation;
    }

    public void setShowAnimation(boolean showAnimation) {
        this.showAnimation = showAnimation;
    }

    public EasingFunctions getEasingFunction() {
        return easingFunction;
    }

    public void setEasingFunction(EasingFunctions easingFunction) {
        this.easingFunction = easingFunction;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
