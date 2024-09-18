package com.hyfata.najoan.koreanpatch.util;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.FormattedCharSequence;
import org.joml.Matrix4f;

public class RenderUtil {
    static Minecraft client = Minecraft.getInstance();

    public static void drawCenteredText(GuiGraphics context, FormattedCharSequence text, float x, float y) {
        Font textRenderer = client.font;
        float textWidth = textRenderer.width(text);
        float xPosition = x - textWidth / 2.0f;
        float yPosition = y - client.font.lineHeight / 2.0f;
        drawText(context, text, xPosition, yPosition);
    }

    public static void drawText(GuiGraphics context, FormattedCharSequence text, float x, float y) {
        Font textRenderer = client.font;
        Matrix4f matrix = context.pose().last().pose();
        MultiBufferSource vertexConsumers = context.bufferSource();
        textRenderer.drawInBatch(text, x, y, -1, false, matrix, vertexConsumers, Font.DisplayMode.NORMAL, 0, 15728880);
    }

    public static void fill(GuiGraphics context, float x1, float y1, float x2, float y2, int color) {
        Matrix4f matrix = context.pose().last().pose();
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

        VertexConsumer vertexConsumer = context.bufferSource().getBuffer(RenderType.gui());
        vertexConsumer.addVertex(matrix, x1, y1, 0f).setColor(color);
        vertexConsumer.addVertex(matrix, x1, y2, 0f).setColor(color);
        vertexConsumer.addVertex(matrix, x2, y2, 0f).setColor(color);
        vertexConsumer.addVertex(matrix, x2, y1, 0f).setColor(color);
        context.flush();
    }
}
