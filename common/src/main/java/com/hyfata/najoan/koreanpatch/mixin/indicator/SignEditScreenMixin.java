package com.hyfata.najoan.koreanpatch.mixin.indicator;

import com.hyfata.najoan.koreanpatch.indicator.AnimationHandler;
import com.hyfata.najoan.koreanpatch.indicator.IndicatorHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractSignEditScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import org.joml.Vector2f;
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
    protected SignBlockEntity sign;

    @Unique
    public final Minecraft koreanPatch$client = Minecraft.getInstance();

    @Unique
    private final AnimationHandler koreanPatch$animationHandler = new AnimationHandler();

    protected SignEditScreenMixin(Component title) {
        super(title);
    }

    @Inject(at = {@At(value = "TAIL")}, method = {"extractSignText"})
    public void addCustomLabel(GuiGraphicsExtractor guiGraphics, Vector2f textOrigin, CallbackInfo ci) {
        float x = -(sign.getMaxTextLineWidth() / 2f) - IndicatorHandler.getIndicatorWidth() / 2 - 5;
        int l = 4 * sign.getTextLineHeight() / 2;
        float y = line * sign.getTextLineHeight() - l + koreanPatch$client.font.lineHeight / 2f;

        koreanPatch$animationHandler.init(0, y - 4);
        koreanPatch$animationHandler.calculateAnimation(0, y);

        IndicatorHandler.showCenteredIndicator(guiGraphics, x, koreanPatch$animationHandler.getResultY());
    }
}

