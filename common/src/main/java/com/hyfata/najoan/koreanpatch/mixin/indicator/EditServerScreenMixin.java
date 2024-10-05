package com.hyfata.najoan.koreanpatch.mixin.indicator;

import com.hyfata.najoan.koreanpatch.util.minecraft.EditBoxUtil;
import com.hyfata.najoan.koreanpatch.util.animation.AnimationUtil;
import com.hyfata.najoan.koreanpatch.handler.Indicator;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.EditServerScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = {EditServerScreen.class})
public class EditServerScreenMixin extends Screen {
    protected EditServerScreenMixin(Component title) {
        super(title);
    }

    @Shadow
    private EditBox ipEdit;

    @Shadow
    private EditBox nameEdit;

    @Shadow
    @Final
    private static Component NAME_LABEL;

    @Shadow
    @Final
    private static Component IP_LABEL;

    @Unique
    private final AnimationUtil koreanPatch$animationUtil = new AnimationUtil();

    @Inject(at = {@At(value = "TAIL")}, method = {"render"})
    private void addCustomLabel(PoseStack context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        float x;
        float y;
        int textX = this.width / 2 - 100 + 1;

        if (nameEdit.isFocused()) {
            x = EditBoxUtil.getCursorXWithText(nameEdit, NAME_LABEL, textX);
            y = EditBoxUtil.calculateIndicatorY(nameEdit);
        } else if (ipEdit.isFocused()) {
            x = EditBoxUtil.getCursorXWithText(ipEdit, IP_LABEL, textX);
            y = EditBoxUtil.calculateIndicatorY(ipEdit);
        } else {
            return;
        }

        koreanPatch$animationUtil.init(x - 4, 0);
        koreanPatch$animationUtil.calculateAnimation(x, 0);

        context.translate(0.0F, 0.0F, 200.0F);
        Indicator.showIndicator(context, koreanPatch$animationUtil.getResultX() + 4, y);
    }
}
