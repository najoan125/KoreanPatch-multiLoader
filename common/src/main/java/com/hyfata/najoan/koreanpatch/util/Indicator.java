package com.hyfata.najoan.koreanpatch.util;

import com.hyfata.najoan.koreanpatch.client.KoreanPatchClient;
import com.hyfata.najoan.koreanpatch.util.language.LanguageUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

public class Indicator {
    static Minecraft client = Minecraft.getInstance();
    private static final float frame = 1f;
    private static final float margin = 1f;

    public static void showIndicator(GuiGraphics context, float x, float y) {
        int rgb = 0x000000;
        int backgroundOpacity = 55 * 255 / 100; // N% * (0 to 255)/100
        int backgroundColor = ((backgroundOpacity & 0xFF) << 24) | rgb; // ARGB
        int frameColor = LanguageUtil.isKorean() ? 0xffff0000 : 0xff00ff00; // ARGB

        if (KoreanPatchClient.IME) {
            frameColor = 0xffffffff;
        }

        float width = (float) LanguageUtil.getCurrentTextWidth();
        float height = (float) client.font.lineHeight;

        renderBox(context, x, y, x + frame + width + margin * 2f, y + frame + height + margin * 2f, frameColor, backgroundColor);
        RenderUtil.drawCenteredText(context, LanguageUtil.getCurrentText(), x + frame + width / 2f + margin, y + frame + height / 2f + margin);
    }

    public static void showIndicator(GuiGraphics context, int x, int y) {
        showIndicator(context, (float) x, (float) y);
    }

    public static void showCenteredIndicator(GuiGraphics context, float x, float y) {
        x -= getIndicatorWidth() / 2f;
        y -= getIndicatorHeight() / 2f;
        showIndicator(context, x, y);
    }

    public static void showCenteredIndicator(GuiGraphics context, int x, int y) {
        showCenteredIndicator(context, (float) x, (float) y);
    }

    public static float getIndicatorWidth() {
        return frame + (float) LanguageUtil.getCurrentTextWidth() + margin * 2f;
    }

    public static float getIndicatorHeight() {
        return frame + (float) client.font.lineHeight + margin * 2f;
    }

    private static void renderBox(GuiGraphics context, float x1, float y1, float x2, float y2, int frameColor, int backgroundColor) {
        RenderUtil.fill(context, x1, y1, x2, y1 + frame, frameColor); // frame with fixed axis-y1
        RenderUtil.fill(context, x1, y2, x2, y2 - frame, frameColor); // frame with fixed axis-y2
        RenderUtil.fill(context, x1, y1, x1 + frame, y2, frameColor); // frame with fixed axis-x1
        RenderUtil.fill(context, x2, y1, x2 - frame, y2, frameColor); // frame with fixed axis-x2

        RenderUtil.fill(context, x1 + frame, y1 + frame, x2 - frame, y2 - frame, backgroundColor); // Background
    }
}
