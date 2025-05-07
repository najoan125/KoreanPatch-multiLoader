package com.hyfata.najoan.koreanpatch.process.handler.indicator;

import com.hyfata.najoan.koreanpatch.data.ConfigManager;
import com.hyfata.najoan.koreanpatch.data.config.category.CategoryIndicator;
import com.hyfata.najoan.koreanpatch.data.config.ColorOpacityConfig;
import com.hyfata.najoan.koreanpatch.data.config.category.indicator.outline.OutlineConfig;
import com.hyfata.najoan.koreanpatch.process.ime.InputManager;
import com.hyfata.najoan.koreanpatch.util.minecraft.RenderUtil;
import com.hyfata.najoan.koreanpatch.data.LangTypeManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

public class IndicatorHandler {
    private static final Minecraft client = Minecraft.getInstance();
    private static final float frame = 1f;
    private static final float margin = 1f;

    public static void showIndicator(GuiGraphics context, float x, float y) {
        CategoryIndicator categoryIndicator = ConfigManager.getInstance().getConfig().getCategoryIndicator();

        if (!categoryIndicator.isShowIndicator()) {
            return;
        }

        float width = (float) LangTypeManager.getInstance().getCurrentTextWidth();
        float height = (float) client.font.lineHeight;

        renderBox(context, x, y, x + frame + width + margin * 2f, y + frame + height + margin * 2f,
                getARGB(categoryIndicator.getOutlineSettings().getColorOpacitySettings()),
                getARGB(categoryIndicator.getBackgroundSettings())
        );

        RenderUtil.drawCenteredText(context, LangTypeManager.getInstance().getCurrentText(),
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
        return frame + (float) LangTypeManager.getInstance().getCurrentTextWidth() + margin * 2f;
    }

    public static float getIndicatorHeight() {
        return frame + (float) client.font.lineHeight + margin * 2f;
    }

    private static int getARGB(ColorOpacityConfig colorOpacityConfig) {
        int outlineRGB = InputManager.getController().isFocused() ?
                colorOpacityConfig.getImeColor().getRGB() :
                LangTypeManager.getInstance().isKorean() ?
                        colorOpacityConfig.getKoreanColor().getRGB() :
                        colorOpacityConfig.getEnColor().getRGB();
        int outlineOpacity = colorOpacityConfig.getOpacity() * 255 / 100; // N% * (0 to 255)/100
        return ((outlineOpacity & 0xFF) << 24) | outlineRGB; // ARGB
    }

    private static void renderBox(GuiGraphics context, float x1, float y1, float x2, float y2, int frameColor, int backgroundColor) {
        OutlineConfig outlineConfig = ConfigManager.getInstance().getConfig().getCategoryIndicator().getOutlineSettings();

        float radius = 3.5f;
        float adjustment = 0.65f;
        float offset = outlineConfig.isShowOutline() ? 0f : frame;

        RenderUtil.fill(context, x1 + frame - offset, y1 + frame - offset, x2 - frame + offset, y2 - frame + offset, backgroundColor); // Background

        if (outlineConfig.isShowOutline()) {
            switch (outlineConfig.getOutlineType()) {
                case RECTANGLE -> {
                    radius = 0f;
                    adjustment = 0f;
                }
                case SUPERELLIPSE -> {
                    float radiusX = radius + 0.5f;
                    float radiusY = radius;
                    float exponent = 2f;
                    adjustment = 0.5f;
                    RenderUtil.drawVertexSuperellipseFrame(context, x1 + radiusX, y1 + radius, radiusX, radiusY, exponent, frameColor, frame, RenderUtil.VertexDirection.TOP_LEFT);
                    RenderUtil.drawVertexSuperellipseFrame(context, x2 - radiusX, y1 + radius, radiusX, radiusY, exponent, frameColor, frame, RenderUtil.VertexDirection.TOP_RIGHT);
                    RenderUtil.drawVertexSuperellipseFrame(context, x1 + radiusX, y2 - radius, radiusX, radiusY, exponent, frameColor, frame, RenderUtil.VertexDirection.BOTTOM_LEFT);
                    RenderUtil.drawVertexSuperellipseFrame(context, x2 - radiusX, y2 - radius, radiusX, radiusY, exponent, frameColor, frame, RenderUtil.VertexDirection.BOTTOM_RIGHT);
                }
                case null, default -> { // CIRCLE
                    RenderUtil.drawVertexCircleFrame(context, x1 + radius, y1 + radius, radius, frameColor, frame, RenderUtil.VertexDirection.TOP_LEFT);
                    RenderUtil.drawVertexCircleFrame(context, x2 - radius, y1 + radius, radius, frameColor, frame, RenderUtil.VertexDirection.TOP_RIGHT);
                    RenderUtil.drawVertexCircleFrame(context, x1 + radius, y2 - radius, radius, frameColor, frame, RenderUtil.VertexDirection.BOTTOM_LEFT);
                    RenderUtil.drawVertexCircleFrame(context, x2 - radius, y2 - radius, radius, frameColor, frame, RenderUtil.VertexDirection.BOTTOM_RIGHT);
                }
            }

            RenderUtil.fill(context, x1 + radius - adjustment, y1, x2 - radius + adjustment, y1 + frame, frameColor); // frame with fixed axis-y1
            RenderUtil.fill(context, x1 + radius - adjustment, y2, x2 - radius + adjustment, y2 - frame, frameColor); // frame with fixed axis-y2
            RenderUtil.fill(context, x1, y1 + radius - adjustment, x1 + frame, y2 - radius + adjustment, frameColor); // frame with fixed axis-x1
            RenderUtil.fill(context, x2, y1 + radius - adjustment, x2 - frame, y2 - radius + adjustment, frameColor); // frame with fixed axis-x2
        }
    }
}
