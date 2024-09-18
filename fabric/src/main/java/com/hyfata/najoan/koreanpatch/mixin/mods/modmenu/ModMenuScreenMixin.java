package com.hyfata.najoan.koreanpatch.mixin.mods.modmenu;

import com.hyfata.najoan.koreanpatch.util.EditBoxUtil;
import com.hyfata.najoan.koreanpatch.util.Indicator;
import com.hyfata.najoan.koreanpatch.util.animation.AnimationUtil;
import com.terraformersmc.modmenu.gui.ModsScreen;
import net.minecraft.client.gui.GuiGraphics;
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
    AnimationUtil animationUtil = new AnimationUtil();

    @Inject(at = @At("TAIL"), method = "render")
    private void render(GuiGraphics context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        float cursorX = EditBoxUtil.getCursorX(searchBox) + 4;
        float y = EditBoxUtil.calculateIndicatorY(searchBox);

        animationUtil.init(cursorX - 4, 0);
        animationUtil.calculateAnimation(cursorX, 0);

        context.pose().translate(0.0F, 0.0F, 200.0F);
        Indicator.showIndicator(context, animationUtil.getResultX(), y);
    }
}
