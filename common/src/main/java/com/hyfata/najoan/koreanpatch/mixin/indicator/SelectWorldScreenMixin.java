package com.hyfata.najoan.koreanpatch.mixin.indicator;

import com.hyfata.najoan.koreanpatch.util.minecraft.EditBoxUtil;
import com.hyfata.najoan.koreanpatch.indicator.AnimationHandler;
import com.hyfata.najoan.koreanpatch.indicator.IndicatorHandler;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value = {SelectWorldScreen.class})
public class SelectWorldScreenMixin extends Screen {

    @Shadow
    protected EditBox searchBox;

    @Unique
    private final AnimationHandler koreanPatch$animationHandler = new AnimationHandler();

    protected SelectWorldScreenMixin(Component title) {
        super(title);
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        float x = EditBoxUtil.getCursorX(searchBox);
        float y = EditBoxUtil.calculateIndicatorY(searchBox);

        koreanPatch$animationHandler.init((float) this.width / 2 - 105, 0);
        koreanPatch$animationHandler.calculateAnimation(x, 0);

        IndicatorHandler.showIndicator(guiGraphics, koreanPatch$animationHandler.getResultX() + 4, y);
    }
}
