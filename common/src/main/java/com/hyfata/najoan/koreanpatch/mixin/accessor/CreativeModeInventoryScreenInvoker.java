package com.hyfata.najoan.koreanpatch.mixin.accessor;

import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value={CreativeModeInventoryScreen.class})
public interface CreativeModeInventoryScreenInvoker {
    @Invoker(value="refreshSearchResults")
    void updateCreativeSearch();
}

