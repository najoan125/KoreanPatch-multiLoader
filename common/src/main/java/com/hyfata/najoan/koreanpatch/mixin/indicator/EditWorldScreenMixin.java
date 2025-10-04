package com.hyfata.najoan.koreanpatch.mixin.indicator;

import com.hyfata.najoan.koreanpatch.indicator.AnimationHandler;
import com.hyfata.najoan.koreanpatch.indicator.IndicatorHandler;
import com.hyfata.najoan.koreanpatch.util.minecraft.EditBoxUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.worldselection.EditWorldScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = {EditWorldScreen.class})
public class EditWorldScreenMixin extends Screen {
    protected EditWorldScreenMixin(Component title) {
        super(title);
    }

    @Shadow
    private EditBox nameEdit;

    @Shadow
    @Final
    private static Component NAME_LABEL;

    @Unique
    private final AnimationHandler koreanPatch$animationHandler = new AnimationHandler();

    @Inject(at = {@At(value = "TAIL")}, method = {"render"})
    public void addCustomLabel(PoseStack poseStack, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        float x = EditBoxUtil.getCursorXWithText(nameEdit, NAME_LABEL, nameEdit.getX()) + 4;
        float y = EditBoxUtil.calculateIndicatorY(nameEdit);

        koreanPatch$animationHandler.init(x - 4, 0);
        koreanPatch$animationHandler.calculateAnimation(x, 0);

        IndicatorHandler.showIndicator(poseStack, koreanPatch$animationHandler.getResultX(), y);
    }
}
