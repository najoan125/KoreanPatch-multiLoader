package com.hyfata.najoan.koreanpatch.mixin.indicator;

import com.hyfata.najoan.koreanpatch.util.minecraft.EditBoxUtil;
import com.hyfata.najoan.koreanpatch.util.animation.AnimationUtil;
import com.hyfata.najoan.koreanpatch.handler.Indicator;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.DirectJoinServerScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = {DirectJoinServerScreen.class})
public class DirectJoinServerScreenMixin extends Screen {
    @Shadow
    private EditBox ipEdit;

    @Unique
    AnimationUtil koreanPatch$animationUtil = new AnimationUtil();

    protected DirectJoinServerScreenMixin(Component title) {
        super(title);
    }

    @Inject(at = {@At(value = "TAIL")}, method = {"render"})
    private void addCustomLabel(PoseStack context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        float x = EditBoxUtil.getCursorX(ipEdit) + 4;
        float y = EditBoxUtil.calculateIndicatorY(ipEdit);

        koreanPatch$animationUtil.init(x - 4, 0);
        koreanPatch$animationUtil.calculateAnimation(x, 0);

        Indicator.showIndicator(context, koreanPatch$animationUtil.getResultX(), y);
    }
}
