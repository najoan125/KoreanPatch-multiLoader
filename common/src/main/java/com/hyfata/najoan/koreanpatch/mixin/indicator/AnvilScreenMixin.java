package com.hyfata.najoan.koreanpatch.mixin.indicator;

import com.hyfata.najoan.koreanpatch.indicator.IndicatorHandler;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = {AnvilScreen.class})
public class AnvilScreenMixin extends Screen {

    @Shadow
    private EditBox name;

    protected AnvilScreenMixin(Component title) {
        super(title);
    }

    @Inject(at = {@At(value = "TAIL")}, method = {"extractBackground"})
    private void customLabel(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        float x = name.getX() + name.getWidth() - IndicatorHandler.getIndicatorWidth();
        float y = name.getY() - IndicatorHandler.getIndicatorHeight() - 6;

        IndicatorHandler.showIndicator(guiGraphics, x, y);
    }
}
