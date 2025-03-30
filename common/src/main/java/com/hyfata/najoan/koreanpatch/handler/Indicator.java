package com.hyfata.najoan.koreanpatch.handler;

import com.hyfata.najoan.koreanpatch.client.KoreanPatchClient;
import com.hyfata.najoan.koreanpatch.config.indicator.CategoryIndicator;
import com.hyfata.najoan.koreanpatch.config.indicator.ColorOpacityConfig;
import com.hyfata.najoan.koreanpatch.config.indicator.outline.Outline;
import com.hyfata.najoan.koreanpatch.util.minecraft.RenderUtil;
import com.hyfata.najoan.koreanpatch.util.language.LanguageUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

public class Indicator {
    private static final Minecraft client = Minecraft.getInstance();
    private static final float frame = 1f;
    private static final float margin = 1f;

    public static void showIndicator(GuiGraphics context, float x, float y) {
        CategoryIndicator categoryIndicator = KoreanPatchClient.config.getCategoryIndicator();

        if (!categoryIndicator.isShowIndicator()) {
            return;
        }

        float width = (float) LanguageUtil.getCurrentTextWidth();
        float height = (float) client.font.lineHeight;

        renderBox(context, x, y, x + frame + width + margin * 2f, y + frame + height + margin * 2f,
                getARGB(categoryIndicator.getOutlineSettings().getColorOpacitySettings()),
                getARGB(categoryIndicator.getBackgroundSettings())
        );

        RenderUtil.drawCenteredText(context, LanguageUtil.getCurrentText(),
                x + frame + width / 2f + margin, y + frame + height / 2f + margin,
                getARGB(categoryIndicator.getTextSettings())
        );
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

    private static int getARGB(ColorOpacityConfig colorOpacityConfig) {
        int outlineRGB = KoreanPatchClient.IME ?
                colorOpacityConfig.getImeColor() :
                LanguageUtil.isKorean() ?
                        colorOpacityConfig.getKoreanColor() :
                        colorOpacityConfig.getEnColor();
        int outlineOpacity = colorOpacityConfig.getOpacity() * 255 / 100; // N% * (0 to 255)/100
        return ((outlineOpacity & 0xFF) << 24) | outlineRGB; // ARGB
    }

    private static void renderBox(GuiGraphics context, float x1, float y1, float x2, float y2, int frameColor, int backgroundColor) {
        Outline outline = KoreanPatchClient.config.getCategoryIndicator().getOutlineSettings();

        float radius = outline.isRounded() ? 3.5f : 0f;
        float adjustment = outline.isRounded() ? 0.65f : 0f;

        RenderUtil.fill(context, x1 + frame, y1 + frame, x2 - frame, y2 - frame, backgroundColor); // Background

        if (outline.isShowOutline()) {
            if (outline.isRounded()) {
                RenderUtil.drawVertexCircleFrame(context, x1 + radius, y1 + radius, radius, frameColor, frame, RenderUtil.VertexDirection.TOP_LEFT);
                RenderUtil.drawVertexCircleFrame(context, x2 - radius, y1 + radius, radius, frameColor, frame, RenderUtil.VertexDirection.TOP_RIGHT);
                RenderUtil.drawVertexCircleFrame(context, x1 + radius, y2 - radius, radius, frameColor, frame, RenderUtil.VertexDirection.BOTTOM_LEFT);
                RenderUtil.drawVertexCircleFrame(context, x2 - radius, y2 - radius, radius, frameColor, frame, RenderUtil.VertexDirection.BOTTOM_RIGHT);
            }

            RenderUtil.fill(context, x1 + radius - adjustment, y1, x2 - radius + adjustment, y1 + frame, frameColor); // frame with fixed axis-y1
            RenderUtil.fill(context, x1 + radius - adjustment, y2, x2 - radius + adjustment, y2 - frame, frameColor); // frame with fixed axis-y2
            RenderUtil.fill(context, x1, y1 + radius - adjustment, x1 + frame, y2 - radius + adjustment, frameColor); // frame with fixed axis-x1
            RenderUtil.fill(context, x2, y1 + radius - adjustment, x2 - frame, y2 - radius + adjustment, frameColor); // frame with fixed axis-x2
        }
    }
}
