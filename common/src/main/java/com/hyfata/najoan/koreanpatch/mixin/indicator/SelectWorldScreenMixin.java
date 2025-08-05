package com.hyfata.najoan.koreanpatch.mixin.indicator;

import com.hyfata.najoan.koreanpatch.util.minecraft.EditBoxUtil;
import com.hyfata.najoan.koreanpatch.indicator.AnimationHandler;
import com.hyfata.najoan.koreanpatch.indicator.IndicatorHandler;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = {SelectWorldScreen.class})
public class SelectWorldScreenMixin extends Screen {

    @Shadow
    protected EditBox searchBox;

    @Unique
    private final AnimationHandler koreanPatch$animationHandler = new AnimationHandler();

    protected SelectWorldScreenMixin(Component title) {
        super(title);
    }

    @Inject(at = {@At(value = "TAIL")}, method = {"render"})
    private void addCustomLabel(PoseStack context, int mouseX, int mouseY, float delta, CallbackInfo ci){
        float x = EditBoxUtil.getCursorX(searchBox);
        float y = EditBoxUtil.calculateIndicatorY(searchBox);

        koreanPatch$animationHandler.init((float) this.width / 2 - 105, 0);
        koreanPatch$animationHandler.calculateAnimation(x, 0);

        context.translate(0.0F, 0.0F, 200.0F);
        IndicatorHandler.showIndicator(context, koreanPatch$animationHandler.getResultX() + 4, y);
    }
}
