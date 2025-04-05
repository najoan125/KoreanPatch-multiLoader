package com.hyfata.najoan.koreanpatch.data.config.category.indicator.outline;

import com.hyfata.najoan.koreanpatch.data.config.ColorOpacityConfig;

public class OutlineColorConfig implements ColorOpacityConfig {
    int koreanColor = 0xff0000;
    int enColor = 0x00ff00;
    int imeColor = 0xffffff;
    int opacity = 100;

    public OutlineColorConfig() {}

    public OutlineColorConfig(int koreanColor, int enColor, int imeColor, int opacity) {
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
