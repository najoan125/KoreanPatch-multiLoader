package com.hyfata.najoan.koreanpatch.mixin.indicator;

import com.hyfata.najoan.koreanpatch.util.Indicator;
import net.minecraft.client.gui.GuiGraphics;
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
    boolean search = false;

    @Inject(method = {"render"}, at = @At(value = "TAIL", shift = At.Shift.BY, by = -3))
    private void addCustomLabel(GuiGraphics context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (search) {
            int x = searchBox.getX() + searchBox.getWidth() + 19;
            int y = searchBox.getY() + searchBox.getHeight() / 2;

            Indicator.showCenteredIndicator(context, x, y);
        }
    }

    @Inject(at = {@At(value = "HEAD")}, method = {"selectTab"})
    private void check(CreativeModeTab group, CallbackInfo callbackInfo) {
        search = group.getType() == CreativeModeTab.Type.SEARCH;
    }
}
