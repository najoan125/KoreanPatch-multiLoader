package com.hyfata.najoan.koreanpatch.mixin.indicator;

import com.hyfata.najoan.koreanpatch.helper.BookScreenVar;
import com.hyfata.najoan.koreanpatch.indicator.IndicatorHandler;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.BookSignScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BookSignScreen.class)
public abstract class BookSignScreenMixin extends Screen {
    protected BookSignScreenMixin(Component title) {
        super(title);
    }

    @Inject(at = @At("TAIL"), method = "render")
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        float x = (this.width - 192) / 2f;
        float y = 50 + 4.5f;

        BookScreenVar.animationHandler.calculateAnimation(0, y);
        IndicatorHandler.showCenteredIndicator(guiGraphics, x + 10, BookScreenVar.animationHandler.getResultY());
    }
}
