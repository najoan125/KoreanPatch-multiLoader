package com.hyfata.najoan.koreanpatch.handler;

import com.hyfata.najoan.koreanpatch.client.KoreanPatchClient;
import com.hyfata.najoan.koreanpatch.util.minecraft.RenderUtil;
import com.hyfata.najoan.koreanpatch.util.language.LanguageUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

public class Indicator {
    static Minecraft client = Minecraft.getInstance();
    private static final float frame = 0.6f;
    private static final float margin = 1f;

    public static void showIndicator(GuiGraphics context, float x, float y) {
        int rgb = 0x000000;
        int backgroundOpacity = 50 * 255 / 100; // N% * (0 to 255)/100
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
        float radius = 2f;
        float adjustment = 0.4f;

        RenderUtil.fill(context, x1 + frame, y1 + frame, x2 - frame, y2 - frame, backgroundColor); // Background

        RenderUtil.drawVertexCircleFrame(context, x1 + radius, y1 + radius, radius, frameColor, frame, RenderUtil.VertexDirection.TOP_LEFT);
        RenderUtil.drawVertexCircleFrame(context, x2 - radius, y1 + radius, radius, frameColor, frame, RenderUtil.VertexDirection.TOP_RIGHT);
        RenderUtil.drawVertexCircleFrame(context, x1 + radius, y2 - radius, radius, frameColor, frame, RenderUtil.VertexDirection.BOTTOM_LEFT);
        RenderUtil.drawVertexCircleFrame(context, x2 - radius, y2 - radius, radius, frameColor, frame, RenderUtil.VertexDirection.BOTTOM_RIGHT);

        RenderUtil.fill(context, x1 + radius - adjustment, y1, x2 - radius + adjustment, y1 + frame, frameColor); // frame with fixed axis-y1
        RenderUtil.fill(context, x1 + radius - adjustment, y2, x2 - radius + adjustment, y2 - frame, frameColor); // frame with fixed axis-y2
        RenderUtil.fill(context, x1, y1 + radius - adjustment, x1 + frame, y2 - radius + adjustment, frameColor); // frame with fixed axis-x1
        RenderUtil.fill(context, x2, y1 + radius - adjustment, x2 - frame, y2 - radius + adjustment, frameColor); // frame with fixed axis-x2
    }
}
