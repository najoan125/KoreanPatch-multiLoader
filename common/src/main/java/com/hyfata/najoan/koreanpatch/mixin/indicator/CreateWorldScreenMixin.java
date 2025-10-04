package com.hyfata.najoan.koreanpatch.mixin.indicator;

import com.hyfata.najoan.koreanpatch.client.GUIStatus;
import com.hyfata.najoan.koreanpatch.util.minecraft.EditBoxUtil;
import com.hyfata.najoan.koreanpatch.indicator.AnimationHandler;
import com.hyfata.najoan.koreanpatch.indicator.IndicatorHandler;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = {CreateWorldScreen.class})
public class CreateWorldScreenMixin extends Screen {
    protected CreateWorldScreenMixin(Component title) {
        super(title);
    }

    @Shadow
    private EditBox nameEdit;

    @Shadow
    private boolean worldGenSettingsVisible;

    @Unique
    private final AnimationHandler koreanPatch$animationHandler = new AnimationHandler();

    @Inject(at = {@At(value = "RETURN")}, method = {"render"})
    private void addCustomLabel(PoseStack poseStack, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (!worldGenSettingsVisible) {
            GUIStatus.getInstance().setBypassInjection(false);
            Component text = new TranslatableComponent("selectWorld.enterName");

            float x = EditBoxUtil.getCursorXWithText(nameEdit, text, nameEdit.x) + 4;
            float y = EditBoxUtil.calculateIndicatorY(nameEdit);

            koreanPatch$animationHandler.init(x - 4, 0);
            koreanPatch$animationHandler.calculateAnimation(x, 0);

            IndicatorHandler.showIndicator(poseStack, koreanPatch$animationHandler.getResultX(), y);
        } else {
            GUIStatus.getInstance().setBypassInjection(true);
        }
    }
}
