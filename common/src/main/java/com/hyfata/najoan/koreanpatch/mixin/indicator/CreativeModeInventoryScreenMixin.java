package com.hyfata.najoan.koreanpatch.mixin.indicator;

import com.hyfata.najoan.koreanpatch.handler.Indicator;
import com.hyfata.najoan.koreanpatch.platform.Services;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = CreativeModeInventoryScreen.class)
public class CreativeModeInventoryScreenMixin extends Screen {
    @Shadow private EditBox searchBox;

    protected CreativeModeInventoryScreenMixin(Component title) {
        super(title);
    }

    @Unique
    boolean koreanPatch$search = false;

    @Unique
    int koreanPatch$modifier = 19;

    @Inject(method = {"render"}, at = @At(value = "TAIL", shift = At.Shift.BY, by = -3))
    private void addCustomLabel(PoseStack context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (koreanPatch$search) {
            int x = searchBox.x + searchBox.getWidth() + koreanPatch$modifier;
            int y = searchBox.y + searchBox.getHeight() / 2;

            Indicator.showCenteredIndicator(context, x, y);
        }
    }

    @Inject(at = {@At(value = "HEAD")}, method = {"selectTab"})
    private void check(CreativeModeTab group, CallbackInfo callbackInfo) {
        koreanPatch$search = group == CreativeModeTab.TAB_SEARCH;
        if (!Services.PLATFORM.getPlatformName().equals("Fabric")) {
            koreanPatch$modifier = 10;
        }
    }
}
