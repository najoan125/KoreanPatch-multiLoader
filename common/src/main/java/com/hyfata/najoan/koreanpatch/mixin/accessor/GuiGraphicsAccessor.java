package com.hyfata.najoan.koreanpatch.mixin.accessor;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.render.state.GuiRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GuiGraphics.class)
public interface GuiGraphicsAccessor {
    @Accessor
    GuiRenderState getGuiRenderState();
    @Accessor
    GuiGraphics.ScissorStack getScissorStack();
}
