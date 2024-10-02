package com.hyfata.najoan.koreanpatch.mixin.indicator;

import com.hyfata.najoan.koreanpatch.util.animation.AnimationUtil;
import com.hyfata.najoan.koreanpatch.handler.Indicator;
import com.hyfata.najoan.koreanpatch.util.minecraft.EditBoxUtil;
import net.minecraft.client.gui.GuiGraphics;
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
    AnimationUtil koreanPatch$animationUtil = new AnimationUtil();

    @Inject(at = {@At(value = "TAIL")}, method = {"render"})
    public void addCustomLabel(GuiGraphics context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        float x = EditBoxUtil.getCursorXWithText(nameEdit, NAME_LABEL, nameEdit.getX()) + 4;
        float y = EditBoxUtil.calculateIndicatorY(nameEdit);

        koreanPatch$animationUtil.init(x - 4, 0);
        koreanPatch$animationUtil.calculateAnimation(x, 0);

        Indicator.showIndicator(context, koreanPatch$animationUtil.getResultX(), y);
    }
}
