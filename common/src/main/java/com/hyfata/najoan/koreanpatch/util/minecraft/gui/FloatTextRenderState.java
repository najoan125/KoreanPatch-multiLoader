package com.hyfata.najoan.koreanpatch.util.minecraft.gui;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.state.GuiTextRenderState;
import net.minecraft.util.FormattedCharSequence;
import org.joml.Matrix3x2f;
import org.jspecify.annotations.NonNull;

public class FloatTextRenderState extends GuiTextRenderState {
    private Font.PreparedText preparedText;
    private ScreenRectangle bounds;
    private final float x;
    private final float y;
    private final boolean includeEmpty;

    public FloatTextRenderState(Font font, FormattedCharSequence text, Matrix3x2f pose, float x, float y, int color, int backgroundColor, boolean dropShadow, boolean includeEmpty, ScreenRectangle scissor) {
        super(font, text, pose, (int) x, (int) y, color, backgroundColor, dropShadow, includeEmpty, scissor);
        this.includeEmpty = includeEmpty;
        this.x = x;
        this.y = y;
    }

    @Override
    public Font.@NonNull PreparedText ensurePrepared() {
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
