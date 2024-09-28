package com.hyfata.najoan.koreanpatch.mixin.indicator;

import com.hyfata.najoan.koreanpatch.util.animation.AnimationUtil;
import com.hyfata.najoan.koreanpatch.handler.Indicator;
import com.hyfata.najoan.koreanpatch.util.minecraft.EditBoxUtil;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.worldselection.EditWorldScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value = {EditWorldScreen.class})
public class EditWorldScreenMixin extends Screen {
    protected EditWorldScreenMixin(Component title) {
        super(title);
    }

    @Unique
    @Mutable
    @Final
    private EditBox koreanPatch$nameEdit;

    @Shadow
    @Final
    private static Component NAME_LABEL;

    @Unique
    AnimationUtil koreanPatch$animationUtil = new AnimationUtil();

    @Inject(method = "<init>", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void onInit(Minecraft $$0, LevelStorageSource.LevelStorageAccess $$1, String $$2, BooleanConsumer $$3, CallbackInfo ci, Font $$4, EditBox $$5, LinearLayout $$6, Button $$7) {
        koreanPatch$nameEdit = $$5;
    }

    @Inject(at = {@At(value = "TAIL")}, method = {"render"})
    public void addCustomLabel(GuiGraphics context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        float x = EditBoxUtil.getCursorXWithText(koreanPatch$nameEdit, NAME_LABEL, koreanPatch$nameEdit.getX()) + 4;
        float y = EditBoxUtil.calculateIndicatorY(koreanPatch$nameEdit);

        koreanPatch$animationUtil.init(x - 4, 0);
        koreanPatch$animationUtil.calculateAnimation(x, 0);

        Indicator.showIndicator(context, koreanPatch$animationUtil.getResultX(), y);
    }
}
