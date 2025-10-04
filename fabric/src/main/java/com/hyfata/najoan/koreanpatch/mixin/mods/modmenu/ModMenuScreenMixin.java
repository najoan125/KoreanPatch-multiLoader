package com.hyfata.najoan.koreanpatch.mixin.mods.modmenu;

import com.hyfata.najoan.koreanpatch.util.minecraft.EditBoxUtil;
import com.hyfata.najoan.koreanpatch.indicator.IndicatorHandler;
import com.hyfata.najoan.koreanpatch.indicator.AnimationHandler;
import com.mojang.blaze3d.vertex.PoseStack;
import com.terraformersmc.modmenu.gui.ModsScreen;
import net.minecraft.client.gui.components.EditBox;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ModsScreen.class)
public class ModMenuScreenMixin {
    @Shadow
    private EditBox searchBox;

    @Unique
    private final AnimationHandler animationHandler = new AnimationHandler();

    @Inject(at = @At("TAIL"), method = "render")
    private void render(PoseStack poseStack, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        float cursorX = EditBoxUtil.getCursorX(searchBox) + 4;
        float y = EditBoxUtil.calculateIndicatorY(searchBox);

        animationHandler.init(cursorX - 4, 0);
        animationHandler.calculateAnimation(cursorX, 0);

        poseStack.translate(0.0F, 0.0F, 200.0F);
        IndicatorHandler.showIndicator(poseStack, animationHandler.getResultX(), y);
    }
}
