package com.hyfata.najoan.koreanpatch.indicator;

import com.hyfata.najoan.koreanpatch.config.ConfigManager;
import com.hyfata.najoan.koreanpatch.config.category.CategoryIndicator;
import com.hyfata.najoan.koreanpatch.config.ColorOpacityConfig;
import com.hyfata.najoan.koreanpatch.config.category.indicator.outline.OutlineConfig;
import com.hyfata.najoan.koreanpatch.driver.InputManager;
import com.hyfata.najoan.koreanpatch.util.minecraft.RenderUtil;
import com.hyfata.najoan.koreanpatch.process.LangTypeManager;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;

public class IndicatorHandler {
    private static final Minecraft client = Minecraft.getInstance();
    private static final float frame = 1f;
    private static final float margin = 1f;

    public static void showIndicator(PoseStack poseStack, float x, float y) {
        CategoryIndicator categoryIndicator = ConfigManager.getInstance().getConfig().getCategoryIndicator();

        if (!categoryIndicator.isShowIndicator()) {
            return;
        }

        float width = (float) LangTypeManager.getInstance().getCurrentTextWidth();
        float height = (float) client.font.lineHeight;

        renderBox(poseStack, x, y, x + frame + width + margin * 2f, y + frame + height + margin * 2f,
                getARGB(categoryIndicator.getOutlineSettings().getColorOpacitySettings()),
                getARGB(categoryIndicator.getBackgroundSettings())
        );

        RenderUtil.drawCenteredText(poseStack, LangTypeManager.getInstance().getCurrentText(),
                x + frame + width / 2f + margin, y + frame + height / 2f + margin,
                getARGB(categoryIndicator.getTextSettings())
        );
    }

    public static void showIndicator(PoseStack poseStack, int x, int y) {
        showIndicator(poseStack, (float) x, (float) y);
    }

    public static void showCenteredIndicator(PoseStack poseStack, float x, float y) {
        x -= getIndicatorWidth() / 2f;
        y -= getIndicatorHeight() / 2f;
        showIndicator(poseStack, x, y);
    }

    public static void showCenteredIndicator(PoseStack poseStack, int x, int y) {
        showCenteredIndicator(poseStack, (float) x, (float) y);
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
        return ((outlineOpacity & 0xFF) << 24) | (outlineRGB & 0x00ffffff); // ARGB
    }

    private static void renderBox(PoseStack poseStack, float x1, float y1, float x2, float y2, int frameColor, int backgroundColor) {
        OutlineConfig outlineConfig = ConfigManager.getInstance().getConfig().getCategoryIndicator().getOutlineSettings();

        float radius = 3.5f;
        float adjustment = 0.65f;
        float offset = outlineConfig.isShowOutline() ? 0f : frame;

        RenderUtil.fill(poseStack, x1 + frame - offset, y1 + frame - offset, x2 - frame + offset, y2 - frame + offset, backgroundColor); // Background

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
                    RenderUtil.drawVertexSuperellipseFrame(poseStack, x1 + radiusX, y1 + radius, radiusX, radiusY, exponent, frameColor, frame, RenderUtil.VertexDirection.TOP_LEFT);
                    RenderUtil.drawVertexSuperellipseFrame(poseStack, x2 - radiusX, y1 + radius, radiusX, radiusY, exponent, frameColor, frame, RenderUtil.VertexDirection.TOP_RIGHT);
                    RenderUtil.drawVertexSuperellipseFrame(poseStack, x1 + radiusX, y2 - radius, radiusX, radiusY, exponent, frameColor, frame, RenderUtil.VertexDirection.BOTTOM_LEFT);
                    RenderUtil.drawVertexSuperellipseFrame(poseStack, x2 - radiusX, y2 - radius, radiusX, radiusY, exponent, frameColor, frame, RenderUtil.VertexDirection.BOTTOM_RIGHT);
                }
                default -> { // CIRCLE
                    RenderUtil.drawVertexCircleFrame(poseStack, x1 + radius, y1 + radius, radius, frameColor, frame, RenderUtil.VertexDirection.TOP_LEFT);
                    RenderUtil.drawVertexCircleFrame(poseStack, x2 - radius, y1 + radius, radius, frameColor, frame, RenderUtil.VertexDirection.TOP_RIGHT);
                    RenderUtil.drawVertexCircleFrame(poseStack, x1 + radius, y2 - radius, radius, frameColor, frame, RenderUtil.VertexDirection.BOTTOM_LEFT);
                    RenderUtil.drawVertexCircleFrame(poseStack, x2 - radius, y2 - radius, radius, frameColor, frame, RenderUtil.VertexDirection.BOTTOM_RIGHT);
                }
            }

            RenderUtil.fill(poseStack, x1 + radius - adjustment, y1, x2 - radius + adjustment, y1 + frame, frameColor); // frame with fixed axis-y1
            RenderUtil.fill(poseStack, x1 + radius - adjustment, y2, x2 - radius + adjustment, y2 - frame, frameColor); // frame with fixed axis-y2
            RenderUtil.fill(poseStack, x1, y1 + radius - adjustment, x1 + frame, y2 - radius + adjustment, frameColor); // frame with fixed axis-x1
            RenderUtil.fill(poseStack, x2, y1 + radius - adjustment, x2 - frame, y2 - radius + adjustment, frameColor); // frame with fixed axis-x2
        }
    }
}
