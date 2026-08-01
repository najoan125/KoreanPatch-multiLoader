package com.hyfata.najoan.koreanpatch.util.minecraft.gui;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.renderer.state.gui.GuiElementRenderState;
import org.joml.Matrix3x2f;

public record FloatRenderState(RenderPipeline pipeline, TextureSetup textureSetup, Matrix3x2f pose, float x0, float y0, float x1, float y1, int col1, int col2, ScreenRectangle scissorArea, ScreenRectangle bounds) implements GuiElementRenderState {
    public FloatRenderState(RenderPipeline pipeline, TextureSetup textureSetup, Matrix3x2f pose, float x0, float y0, float x1, float y1, int col1, int col2, ScreenRectangle scissorArea) {
        this(pipeline, textureSetup, pose, x0, y0, x1, y1, col1, col2, scissorArea, getBounds((int) x0, (int) y0, (int) x1, (int) y1, pose, scissorArea));
    }

    @Override
    public void buildVertices(VertexConsumer vertexConsumer) {
        vertexConsumer.addVertexWith2DPose(this.pose(), this.x0(), this.y0()).setColor(this.col1());
        vertexConsumer.addVertexWith2DPose(this.pose(), this.x0(), this.y1()).setColor(this.col2());
        vertexConsumer.addVertexWith2DPose(this.pose(), this.x1(), this.y1()).setColor(this.col2());
        vertexConsumer.addVertexWith2DPose(this.pose(), this.x1(), this.y0()).setColor(this.col1());
    }

    private static ScreenRectangle getBounds(int x0, int y0, int x1, int y1, Matrix3x2f pose, ScreenRectangle scissorArea) {
        ScreenRectangle screenrectangle = (new ScreenRectangle(x0, y0, x1 - x0, y1 - y0)).transformMaxBounds(pose);
        return scissorArea != null ? scissorArea.intersection(screenrectangle) : screenrectangle;
    }
}
