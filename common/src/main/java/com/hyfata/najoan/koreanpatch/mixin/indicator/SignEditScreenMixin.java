package com.hyfata.najoan.koreanpatch.mixin.indicator;

import com.hyfata.najoan.koreanpatch.util.animation.AnimationUtil;
import com.hyfata.najoan.koreanpatch.handler.Indicator;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.SignEditScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = {SignEditScreen.class})
public abstract class SignEditScreenMixin extends Screen {
    @Shadow
    private int line;

    @Unique
    public final Minecraft koreanPatch$client = Minecraft.getInstance();

    @Unique
    AnimationUtil koreanPatch$animationUtil = new AnimationUtil();

    protected SignEditScreenMixin(Component title) {
        super(title);
    }

    @Inject(at = {@At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/Lighting;setupFor3DItems()V", shift = At.Shift.BY, by = -3)}, method = {"render"})
    public void addCustomLabel(PoseStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        float x = -(90 / 2f) - Indicator.getIndicatorWidth() / 2 - 5;
        int l = 4 * 10 / 2;
        float y = line * 10 - l + koreanPatch$client.font.lineHeight / 2f;

        koreanPatch$animationUtil.init(0, y - 4);
        koreanPatch$animationUtil.calculateAnimation(0, y);

        Indicator.showCenteredIndicator(matrices, x, koreanPatch$animationUtil.getResultY());
    }
}

