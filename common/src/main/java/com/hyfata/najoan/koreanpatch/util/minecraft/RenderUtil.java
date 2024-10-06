package com.hyfata.najoan.koreanpatch.util.minecraft;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.FormattedCharSequence;

public class RenderUtil {
    static Minecraft client = Minecraft.getInstance();

    public static void drawCenteredText(PoseStack context, FormattedCharSequence text, float x, float y) {
        Font textRenderer = client.font;
        float textWidth = textRenderer.width(text);
        float xPosition = x - textWidth / 2.0f;
        float yPosition = y - client.font.lineHeight / 2.0f;
        drawText(context, text, xPosition, yPosition);
    }

    public static void drawText(PoseStack context, FormattedCharSequence text, float x, float y) {
        Font textRenderer = client.font;
        textRenderer.draw(context, text, x, y, -1);
    }

    public static void fill(PoseStack context, float x1, float y1, float x2, float y2, int color) {
        Matrix4f matrix = context.last().pose();
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

        BufferBuilder bufferBuilder = Tesselator.getInstance().getBuilder();
        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        bufferBuilder.vertex(matrix, x1, y1, 0f).color(color).endVertex();
        bufferBuilder.vertex(matrix, x1, y2, 0f).color(color).endVertex();
        bufferBuilder.vertex(matrix, x2, y2, 0f).color(color).endVertex();
        bufferBuilder.vertex(matrix, x2, y1, 0f).color(color).endVertex();
        bufferBuilder.end();
        BufferUploader.end(bufferBuilder);
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }
}
