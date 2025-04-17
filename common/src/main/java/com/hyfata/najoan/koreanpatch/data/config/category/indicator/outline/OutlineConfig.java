package com.hyfata.najoan.koreanpatch.data.config.category.indicator.outline;

import com.hyfata.najoan.koreanpatch.data.gson.JsonComment;
import com.hyfata.najoan.koreanpatch.data.provider.OutlineType;

public class OutlineConfig {
    private boolean showOutline = true;

    @JsonComment(value = "Outline type: ", enums = true)
    private OutlineType outlineType = OutlineType.CIRCLE;

    private final OutlineColorConfig colorOpacity = new OutlineColorConfig();

    public boolean isShowOutline() {
        return showOutline;
    }

    public void setShowOutline(boolean showOutline) {
        this.showOutline = showOutline;
    }

    public OutlineType getOutlineType() {
        return outlineType;
    }

    public void setOutlineType(OutlineType outlineType) {
        this.outlineType = outlineType;
    }

    public OutlineColorConfig getColorOpacitySettings() {
        return colorOpacity;
    }
}
