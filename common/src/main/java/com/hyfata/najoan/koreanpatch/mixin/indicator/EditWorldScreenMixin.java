package com.hyfata.najoan.koreanpatch.mixin.indicator;

import com.hyfata.najoan.koreanpatch.indicator.AnimationHandler;
import com.hyfata.najoan.koreanpatch.indicator.IndicatorHandler;
import com.hyfata.najoan.koreanpatch.util.minecraft.EditBoxUtil;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.worldselection.EditWorldScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = {EditWorldScreen.class})
public class EditWorldScreenMixin extends Screen {
    protected EditWorldScreenMixin(Component title) {
        super(title);
    }

    @Shadow
    @Final
    private EditBox nameEdit;

    @Shadow @Final
    private static Component NAME_LABEL;

    @Unique
    private final AnimationHandler koreanPatch$animationHandler = new AnimationHandler();

    @Inject(at = {@At(value = "TAIL")}, method = {"extractRenderState"})
    public void addCustomLabel(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        float x = EditBoxUtil.getCursorXWithText(nameEdit, NAME_LABEL, nameEdit.getX()) + 4;
        float y = EditBoxUtil.calculateIndicatorY(nameEdit);

        koreanPatch$animationHandler.init(x - 4, 0);
        koreanPatch$animationHandler.calculateAnimation(x, 0);

        IndicatorHandler.showIndicator(guiGraphics, koreanPatch$animationHandler.getResultX(), y);
    }
}
