package com.hyfata.najoan.koreanpatch.mixin.indicator;

import com.hyfata.najoan.koreanpatch.mixin.accessor.BookEditScreenDisplayCacheAccessor;
import com.hyfata.najoan.koreanpatch.util.animation.AnimationUtil;
import com.hyfata.najoan.koreanpatch.handler.Indicator;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.BookEditScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = {BookEditScreen.class})
public abstract class BookEditScreenMixin extends Screen {

    protected BookEditScreenMixin(Component title) {
        super(title);
    }

    @Shadow
    protected abstract BookEditScreen.DisplayCache getDisplayCache();

    @Shadow
    private boolean isSigning;

    @Unique
    AnimationUtil koreanPatch$animationUtil = new AnimationUtil();

    @Inject(at = {@At(value = "RETURN")}, method = {"render"})
    private void addCustomLabel(PoseStack context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        float x = (this.width - 192) / 2f; // int i = (this.width - 192) / 2; in render() method
        float y;
        if (isSigning) {
            y = 50 + 4.5f;
        } else {
            BookEditScreenDisplayCacheAccessor pageContent = (BookEditScreenDisplayCacheAccessor) getDisplayCache();
            y = pageContent.getCursor().y + 32 + 4.5f; //absolutePositionToScreenPosition() + (fontHeight(9) / 2)
        }

        koreanPatch$animationUtil.init(0, y - 4);
        koreanPatch$animationUtil.calculateAnimation(0, y);

        Indicator.showCenteredIndicator(context, x + 10, koreanPatch$animationUtil.getResultY());
    }
}

