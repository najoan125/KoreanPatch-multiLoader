package com.hyfata.najoan.koreanpatch.mixin.mods.easy_anvils;

import com.hyfata.najoan.koreanpatch.handler.Indicator;
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

    @Inject(at = @At("TAIL"), method = "m_6305_", remap = false)
    private void render(PoseStack guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        float x = name.x - Indicator.getIndicatorWidth() / 2f - 5f;
        float y = name.y + name.getHeight() / 2f - 2f;

        Indicator.showCenteredIndicator(guiGraphics, x, y);
    }
}
