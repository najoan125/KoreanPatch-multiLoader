package com.hyfata.najoan.koreanpatch.config.category.indicator.outline;

import com.hyfata.najoan.koreanpatch.config.ColorOpacityConfig;

import java.awt.*;

public class OutlineColorConfig implements ColorOpacityConfig {
    private Color koreanColor = new Color(0xff0000);
    private Color enColor = new Color(0x00ff00);
    private Color imeColor = new Color(0xffffff);
    private int opacity = 100;

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

    @Override
    public void setOpacity(int opacity) {
        this.opacity = opacity;
    }
}
