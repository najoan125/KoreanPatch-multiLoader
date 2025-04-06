package com.hyfata.najoan.koreanpatch.data.config.category.indicator.outline;

import com.hyfata.najoan.koreanpatch.data.provider.OutlineType;

public class OutlineConfig {
    boolean showOutline = true;
    OutlineType outlineType = OutlineType.CIRCLE;
    OutlineColorConfig colorOpacity = new OutlineColorConfig();

    public OutlineConfig() {}

    public OutlineConfig(boolean showOutline, OutlineType outlineType, OutlineColorConfig colorOpacity) {
        this.showOutline = showOutline;
        this.outlineType = outlineType;
        this.colorOpacity = colorOpacity;
    }

    public boolean isShowOutline() {
        return showOutline;
    }

    public OutlineType getOutlineType() {
        return outlineType;
    }

    public OutlineColorConfig getColorOpacitySettings() {
        return colorOpacity;
    }

    public void setShowOutline(boolean showOutline) {
        this.showOutline = showOutline;
    }

    public void setOutlineType(OutlineType outlineType) {
        this.outlineType = outlineType;
    }

    public void setColorOpacity(OutlineColorConfig colorOpacity) {
        this.colorOpacity = colorOpacity;
    }
}
