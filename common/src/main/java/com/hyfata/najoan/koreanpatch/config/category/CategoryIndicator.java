package com.hyfata.najoan.koreanpatch.config.category;

import com.hyfata.najoan.koreanpatch.config.category.indicator.IndicatorAnimationConfig;
import com.hyfata.najoan.koreanpatch.config.category.indicator.IndicatorBackgroundColorConfig;
import com.hyfata.najoan.koreanpatch.config.category.indicator.IndicatorTextColorConfig;
import com.hyfata.najoan.koreanpatch.config.category.indicator.outline.OutlineConfig;

public class CategoryIndicator {
    private boolean showIndicator = true;
    private final OutlineConfig outlineConfig = new OutlineConfig();
    private final IndicatorBackgroundColorConfig background = new IndicatorBackgroundColorConfig();
    private final IndicatorTextColorConfig text = new IndicatorTextColorConfig();
    private final IndicatorAnimationConfig animation = new IndicatorAnimationConfig();

    public boolean isShowIndicator() {
        return showIndicator;
    }

    public void setShowIndicator(boolean showIndicator) {
        this.showIndicator = showIndicator;
    }

    public OutlineConfig getOutlineSettings() {
        return outlineConfig;
    }

    public IndicatorBackgroundColorConfig getBackgroundSettings() {
        return background;
    }

    public IndicatorTextColorConfig getTextSettings() {
        return text;
    }

    public IndicatorAnimationConfig getAnimationSettings() {
        return animation;
    }
}
