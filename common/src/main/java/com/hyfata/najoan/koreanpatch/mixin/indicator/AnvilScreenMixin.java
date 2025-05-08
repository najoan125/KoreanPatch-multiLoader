package com.hyfata.najoan.koreanpatch.mixin.indicator;

import com.hyfata.najoan.koreanpatch.process.handler.indicator.IndicatorHandler;
import com.mojang.blaze3d.vertex.PoseStack;
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

    @Inject(at = {@At(value = "TAIL")}, method = {"renderFg"})
    private void customLabel(PoseStack context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        float x = name.x + name.getWidth() - IndicatorHandler.getIndicatorWidth();
        float y = name.y - IndicatorHandler.getIndicatorHeight() - 6;

        IndicatorHandler.showIndicator(context, x, y);
    }
}
