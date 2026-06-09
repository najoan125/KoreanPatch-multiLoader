package com.hyfata.najoan.koreanpatch.mixin.mods.easy_anvils;

import com.hyfata.najoan.koreanpatch.indicator.IndicatorHandler;
import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.easyanvils.client.gui.screens.inventory.NameTagEditScreen;
import net.minecraft.client.gui.components.EditBox;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = {NameTagEditScreen.class})
public class NameTagEditScreenMixin {
    @Shadow(remap = false) private EditBox name;

    @Inject(at = @At("TAIL"), method = "m_86412_", remap = false)
    private void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        float x = name.getX() - IndicatorHandler.getIndicatorWidth() / 2f - 5f;
        float y = name.getY() + name.getHeight() / 2f - 2f;

        IndicatorHandler.showCenteredIndicator(poseStack, x, y);
    }
}
