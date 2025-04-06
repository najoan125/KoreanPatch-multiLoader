package com.hyfata.najoan.koreanpatch.data.config.category.indicator.outline;

public class OutlineConfig {
    boolean showOutline = true;
    boolean rounded = true;
    OutlineColorConfig colorOpacity = new OutlineColorConfig();

    public OutlineConfig() {}

    public OutlineConfig(boolean showOutline, boolean rounded, OutlineColorConfig colorOpacity) {
        this.showOutline = showOutline;
        this.rounded = rounded;
        this.colorOpacity = colorOpacity;
    }

    public boolean isShowOutline() {
        return showOutline;
    }

    public boolean isRounded() {
        return rounded;
    }

    public OutlineColorConfig getColorOpacitySettings() {
        return colorOpacity;
    }

    public void setShowOutline(boolean showOutline) {
        this.showOutline = showOutline;
    }

    public void setRounded(boolean rounded) {
        this.rounded = rounded;
    }

    public void setColorOpacity(OutlineColorConfig colorOpacity) {
        this.colorOpacity = colorOpacity;
    }
}
