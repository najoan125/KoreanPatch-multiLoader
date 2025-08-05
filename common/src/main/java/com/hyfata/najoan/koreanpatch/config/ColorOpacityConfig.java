package com.hyfata.najoan.koreanpatch.config;

import java.awt.*;

public interface ColorOpacityConfig {
    int getOpacity();
    Color getKoreanColor();
    Color getEnColor();
    Color getImeColor();
    void setOpacity(int opacity);
    void setKoreanColor(Color koreanColor);
    void setEnColor(Color enColor);
    void setImeColor(Color imeColor);
}
