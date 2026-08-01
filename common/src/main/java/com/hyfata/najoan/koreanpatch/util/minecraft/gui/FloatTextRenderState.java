package com.hyfata.najoan.koreanpatch.util.minecraft.gui;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.renderer.state.gui.GuiTextRenderState;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix3x2f;

public class FloatTextRenderState extends GuiTextRenderState {
    private Font.PreparedText preparedText;
    private ScreenRectangle bounds;
    private final float x;
    private final float y;
    private final boolean includeEmpty;
    // GuiTextRenderState keeps these private since 26.x, so keep our own copies.
    private final Font font;
    private final FormattedCharSequence text;
    private final int color;
    private final int backgroundColor;
    private final boolean dropShadow;

    public FloatTextRenderState(Font font, FormattedCharSequence text, Matrix3x2f pose, float x, float y, int color, int backgroundColor, boolean dropShadow, boolean includeEmpty, ScreenRectangle scissor) {
        super(font, text, pose, (int) x, (int) y, color, backgroundColor, dropShadow, includeEmpty, scissor);
        this.includeEmpty = includeEmpty;
        this.x = x;
        this.y = y;
        this.font = font;
        this.text = text;
        this.color = color;
        this.backgroundColor = backgroundColor;
        this.dropShadow = dropShadow;
    }

    @Override
    public Font.@NotNull PreparedText ensurePrepared() {
        if (this.preparedText == null) {
            this.preparedText = this.font.prepareText(this.text, this.x, this.y, this.color, this.dropShadow, includeEmpty, this.backgroundColor);
            ScreenRectangle screenrectangle = this.preparedText.bounds();
            if (screenrectangle != null) {
                screenrectangle = screenrectangle.transformMaxBounds(this.pose);
                this.bounds = this.scissor != null ? this.scissor.intersection(screenrectangle) : screenrectangle;
            }
        }

        return this.preparedText;
    }

    @Override
    public ScreenRectangle bounds() {
        this.ensurePrepared();
        return this.bounds;
    }
}
