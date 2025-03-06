package com.hyfata.najoan.koreanpatch.mixin.mods.easy_anvils;

import com.hyfata.najoan.koreanpatch.handler.Indicator;
import fuzs.easyanvils.client.gui.screens.inventory.NameTagEditScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = {NameTagEditScreen.class})
public class NameTagEditScreenMixin {
    @Shadow private EditBox name;

    @Inject(at = @At("TAIL"), method = "render")
    private void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        float x = name.getX() - Indicator.getIndicatorWidth() / 2f - 5f;
        float y = name.getY() + name.getHeight() / 2f - 2f;

        Indicator.showCenteredIndicator(guiGraphics, x, y);
    }
}
