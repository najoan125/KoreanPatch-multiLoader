package com.hyfata.najoan.koreanpatch.mixin.mods.bettercommand;

import bettercommandblockui.main.ui.screen.AbstractBetterCommandBlockScreen;
import com.hyfata.najoan.koreanpatch.util.Indicator;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractBetterCommandBlockScreen.class)
public class AbstractBetterCommandBlockScreenMixin {
    @Shadow
    protected EditBox consoleCommandTextField;

    @Inject(method = "render", at = @At("TAIL"))
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        int x = (int) (consoleCommandTextField.getX() - Indicator.getIndicatorWidth() - 10);
        int y = consoleCommandTextField.getY();

        Indicator.showIndicator(context, x, y);
    }
}
