package com.hyfata.najoan.koreanpatch.data.config.category;

import com.hyfata.najoan.koreanpatch.data.config.category.indicator.IndicatorAnimationConfig;
import com.hyfata.najoan.koreanpatch.data.config.category.indicator.IndicatorBackgroundColorConfig;
import com.hyfata.najoan.koreanpatch.data.config.category.indicator.IndicatorTextColorConfig;
import com.hyfata.najoan.koreanpatch.data.config.category.indicator.outline.OutlineConfig;

public class CategoryIndicator {
    boolean showIndicator = true;
    OutlineConfig outlineConfig = new OutlineConfig();
    IndicatorBackgroundColorConfig background = new IndicatorBackgroundColorConfig();
    IndicatorTextColorConfig text = new IndicatorTextColorConfig();
    IndicatorAnimationConfig animation = new IndicatorAnimationConfig();

    public CategoryIndicator() {}

    public CategoryIndicator(boolean showIndicator, OutlineConfig outlineConfig, IndicatorBackgroundColorConfig background, IndicatorTextColorConfig text, IndicatorAnimationConfig animation) {
        this.showIndicator = showIndicator;
        this.outlineConfig = outlineConfig;
        this.background = background;
        this.text = text;
        this.animation = animation;
    }

    public boolean isShowIndicator() {
        return showIndicator;
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
