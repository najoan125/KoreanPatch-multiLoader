package com.hyfata.najoan.koreanpatch.mixin.indicator;

import com.hyfata.najoan.koreanpatch.client.KoreanPatchClient;
import com.hyfata.najoan.koreanpatch.util.minecraft.EditBoxUtil;
import com.hyfata.najoan.koreanpatch.util.animation.AnimationUtil;
import com.hyfata.najoan.koreanpatch.handler.Indicator;
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
    AnimationUtil koreanPatch$animationUtil = new AnimationUtil();

    @Inject(at = {@At(value = "RETURN")}, method = {"render"})
    private void addCustomLabel(PoseStack context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (!worldGenSettingsVisible) {
            KoreanPatchClient.bypassInjection = false;
            Component text = new TranslatableComponent("selectWorld.enterName");

            float x = EditBoxUtil.getCursorXWithText(nameEdit, text, nameEdit.x) + 4;
            float y = EditBoxUtil.calculateIndicatorY(nameEdit);

            koreanPatch$animationUtil.init(x - 4, 0);
            koreanPatch$animationUtil.calculateAnimation(x, 0);

            Indicator.showIndicator(context, koreanPatch$animationUtil.getResultX(), y);
        } else {
            KoreanPatchClient.bypassInjection = true;
        }
    }
}
