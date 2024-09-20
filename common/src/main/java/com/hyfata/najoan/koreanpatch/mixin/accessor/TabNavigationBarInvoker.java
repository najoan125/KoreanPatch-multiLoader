package com.hyfata.najoan.koreanpatch.mixin.accessor;

import net.minecraft.client.gui.components.tabs.TabManager;
import net.minecraft.client.gui.components.tabs.TabNavigationBar;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TabNavigationBar.class)
public interface TabNavigationBarInvoker {
    @Accessor("tabManager")
    TabManager getTabManager();
}
