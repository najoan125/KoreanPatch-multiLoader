package com.hyfata.najoan.koreanpatch.util.minecraft;

import com.hyfata.najoan.koreanpatch.mixin.accessor.GuiGraphicsAccessor;
import com.hyfata.najoan.koreanpatch.util.minecraft.gui.FloatRenderState;
import com.hyfata.najoan.koreanpatch.util.minecraft.gui.FloatTextRenderState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.util.ARGB;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import org.joml.Matrix3x2f;

public class RenderUtil {
    private static final Minecraft client = Minecraft.getInstance();

    public static void drawCenteredText(GuiGraphics guiGraphics, FormattedCharSequence text, float x, float y) {
        drawCenteredText(guiGraphics, text, x, y, -1);
    }

    public static void drawCenteredText(GuiGraphics guiGraphics, FormattedCharSequence text, float x, float y, int color) {
        Font textRenderer = client.font;
        float textWidth = textRenderer.width(text);
        float xPosition = x - textWidth / 2.0f;
        float yPosition = y - client.font.lineHeight / 2.0f;
        drawText(guiGraphics, text, xPosition, yPosition, color);
    }

    public static void drawText(GuiGraphics guiGraphics, FormattedCharSequence text, float x, float y) {
        drawText(guiGraphics, text, x, y, -1);
    }

    public static void drawText(GuiGraphics guiGraphics, FormattedCharSequence text, float x, float y, int color) {
        if (ARGB.alpha(color) != 0) {
            GuiGraphicsAccessor accessor = (GuiGraphicsAccessor) guiGraphics;
            accessor.getGuiRenderState().submitText(
                    new FloatTextRenderState(
                            client.font, text, new Matrix3x2f(guiGraphics.pose()), x, y, color, 0, true, false, accessor.getScissorStack().peek()
                    )
            );
        }
    }

    public static void fill(GuiGraphics guiGraphics, float x1, float y1, float x2, float y2, int color) {
        float i;
        if (x1 < x2) {
            i = x1;
            x1 = x2;
            x2 = i;
        }

        if (y1 < y2) {
            i = y1;
            y1 = y2;
            y2 = i;
        }

        submitColoredRectangle(guiGraphics, TextureSetup.noTexture(), x1, y1, x2, y2, color);
    }

    private static void submitColoredRectangle(GuiGraphics guiGraphics, TextureSetup textureSetup, float x0, float y0, float x1, float y1, int col1) {
        GuiGraphicsAccessor accessor = (GuiGraphicsAccessor) guiGraphics;
        accessor.getGuiRenderState().submitGuiElement(
                new FloatRenderState(
                        RenderPipelines.GUI,
                        textureSetup,
                        new Matrix3x2f(guiGraphics.pose()),
                        x0, y0, x1, y1,
                        col1,
                        col1,
                        accessor.getScissorStack().peek()
                )
        );
    }

    public static void drawVertexCircleFrame(GuiGraphics guiGraphics, float centerX, float centerY, float radius, int frameColor, float frameThickness, VertexDirection direction) {
        // radius = outerRadius
        float innerRadius = radius - frameThickness;
        if (innerRadius < 0) {
            throw new IllegalArgumentException("Frame thickness cannot be greater than the outer radius.");
        }

        int steps = 18;
        float angleStep = Mth.PI / 2f / steps;

        float cos = 1.0f;
        float sin = 0.0f;
        float cosDelta = Mth.cos(angleStep);
        float sinDelta = Mth.sin(angleStep);

        for (int i = 0; i < steps; i++) {
            float tempCos = cos;
            cos = cos * cosDelta - sin * sinDelta;
            sin = tempCos * sinDelta + sin * cosDelta;

            float[] innerPoint = calculateVertexDirectionPoint(centerX, centerY, innerRadius, cos, sin, direction);
            float[] outerPoint = calculateVertexDirectionPoint(centerX, centerY, radius, cos, sin, direction);

            RenderUtil.fill(guiGraphics, innerPoint[0], innerPoint[1], outerPoint[0], outerPoint[1], frameColor);
        }
    }

    public static void drawVertexSuperellipseFrame(GuiGraphics guiGraphics, float centerX, float centerY, float radiusX, float radiusY, float exponent, int frameColor, float frameThickness, VertexDirection direction) {
        int steps = 36;
        float angleStep = Mth.PI / 2f / steps;

        float correctedExponent = 2f / exponent;

        float cos = 1.0f;
        float sin = 0.0f;
        float cosDelta = Mth.cos(angleStep);
        float sinDelta = Mth.sin(angleStep);

        for (int i = 0; i < steps; i++) {
            float tempCos = cos;
            cos = cos * cosDelta - sin * sinDelta;
            sin = tempCos * sinDelta + sin * cosDelta;

            float projX = (float) Math.pow(Mth.abs(cos), correctedExponent);
            float projY = (float) Math.pow(Mth.abs(sin), correctedExponent);

            float[] innerPoint = calculateVertexDirectionPoint(centerX, centerY, radiusX - frameThickness, radiusY - frameThickness, projX, projY, direction);
            float[] outerPoint = calculateVertexDirectionPoint(centerX, centerY, radiusX, radiusY, projX, projY, direction);

            RenderUtil.fill(guiGraphics, innerPoint[0], innerPoint[1], outerPoint[0], outerPoint[1], frameColor);
        }
    }

    private static float[] calculateVertexDirectionPoint(float centerX, float centerY, float radius, float projX, float projY, VertexDirection direction) {
        return calculateVertexDirectionPoint(centerX, centerY, radius, radius, projX, projY, direction);
    }

    private static float[] calculateVertexDirectionPoint(float centerX, float centerY, float radiusX, float radiusY, float projX, float projY, VertexDirection direction) {
        float x, y;
        switch (direction) {
            case TOP_LEFT -> {
                x = centerX - radiusX * projX;
                y = centerY - radiusY * projY;
            }
            case TOP_RIGHT -> {
                x = centerX + radiusX * projX;
                y = centerY - radiusY * projY;
            }
            case BOTTOM_LEFT -> {
                x = centerX - radiusX * projX;
                y = centerY + radiusY * projY;
            }
            case BOTTOM_RIGHT -> {
                x = centerX + radiusX * projX;
                y = centerY + radiusY * projY;
            }
            default -> throw new IllegalArgumentException("Invalid VertexDirection");
        }
        return new float[]{x, y};
    }

    public enum VertexDirection {
        TOP_LEFT,
        TOP_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_RIGHT
    }
}
