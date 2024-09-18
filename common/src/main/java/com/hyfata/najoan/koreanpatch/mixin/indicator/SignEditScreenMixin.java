package com.hyfata.najoan.koreanpatch.mixin.indicator;

import com.hyfata.najoan.koreanpatch.util.animation.AnimationUtil;
import com.hyfata.najoan.koreanpatch.util.Indicator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractSignEditScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = {AbstractSignEditScreen.class})
public abstract class SignEditScreenMixin extends Screen {
    @Shadow
    private int line;

    @Shadow
    @Final
    private SignBlockEntity sign;

    @Unique
    public final Minecraft client = Minecraft.getInstance();

    @Unique
    AnimationUtil animationUtil = new AnimationUtil();

    protected SignEditScreenMixin(Component title) {
        super(title);
    }

    @Inject(at = {@At(value = "TAIL")}, method = {"renderSignText"})
    public void addCustomLabel(GuiGraphics context, CallbackInfo ci) {
        float x = -(sign.getMaxTextLineWidth() / 2f) - Indicator.getIndicatorWidth() / 2 - 5;
        int l = 4 * sign.getTextLineHeight() / 2;
        float y = line * sign.getTextLineHeight() - l + client.font.lineHeight / 2f;

        animationUtil.init(0, y - 4);
        animationUtil.calculateAnimation(0, y);

        Indicator.showCenteredIndicator(context, x, animationUtil.getResultY());
    }
}

