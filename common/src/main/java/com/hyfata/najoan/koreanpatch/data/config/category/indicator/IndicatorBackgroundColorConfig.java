package com.hyfata.najoan.koreanpatch.data.config.category.indicator;

import com.hyfata.najoan.koreanpatch.data.config.ColorOpacityConfig;

import java.awt.*;

public class IndicatorBackgroundColorConfig implements ColorOpacityConfig {
    private Color koreanColor = new Color(0x000000);
    private Color enColor = new Color(0x000000);
    private Color imeColor = new Color(0x000000);
    private int opacity = 50;

    @Override
    public Color getKoreanColor() {
        return koreanColor;
    }

    @Override
    public Color getEnColor() {
        return enColor;
    }

    @Override
    public Color getImeColor() {
        return imeColor;
    }

    @Override
    public int getOpacity() {
        return opacity;
    }

    @Override
    public void setOpacity(int opacity) {
        this.opacity = opacity;
    }

    @Override
    public void setKoreanColor(Color koreanColor) {
        this.koreanColor = koreanColor;
    }

    @Override
    public void setEnColor(Color enColor) {
        this.enColor = enColor;
    }

    @Override
    public void setImeColor(Color imeColor) {
        this.imeColor = imeColor;
    }
}
