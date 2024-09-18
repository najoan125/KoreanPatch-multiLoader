package com.hyfata.najoan.koreanpatch.mixin.indicator;

import com.hyfata.najoan.koreanpatch.util.EditBoxUtil;
import com.hyfata.najoan.koreanpatch.util.animation.AnimationUtil;
import com.hyfata.najoan.koreanpatch.util.Indicator;
import net.minecraft.client.gui.GuiGraphics;
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
    AnimationUtil animationUtil = new AnimationUtil();

    protected DirectJoinServerScreenMixin(Component title) {
        super(title);
    }

    @Inject(at = {@At(value = "TAIL")}, method = {"render"})
    private void addCustomLabel(GuiGraphics context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        float x = EditBoxUtil.getCursorX(ipEdit) + 4;
        float y = EditBoxUtil.calculateIndicatorY(ipEdit);

        animationUtil.init(x - 4, 0);
        animationUtil.calculateAnimation(x, 0);

        Indicator.showIndicator(context, animationUtil.getResultX(), y);
    }
}
