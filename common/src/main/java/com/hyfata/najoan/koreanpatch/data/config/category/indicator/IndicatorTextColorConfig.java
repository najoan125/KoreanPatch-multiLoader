package com.hyfata.najoan.koreanpatch.data.config.category.indicator;

import com.hyfata.najoan.koreanpatch.data.config.ColorOpacityConfig;

public class IndicatorTextColorConfig implements ColorOpacityConfig {
    int koreanColor = 0xffffff;
    int enColor = 0xffffff;
    int imeColor = 0xffffff;
    int opacity = 100;

    public IndicatorTextColorConfig() {}

    public IndicatorTextColorConfig(int koreanColor, int enColor, int imeColor, int opacity) {
        this.koreanColor = koreanColor;
        this.enColor = enColor;
        this.imeColor = imeColor;
        this.opacity = opacity;
    }

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
