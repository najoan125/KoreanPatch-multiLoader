package com.hyfata.najoan.koreanpatch.mixin.accessor;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.gui.components.tabs.Tab;
import net.minecraft.client.gui.components.tabs.TabManager;
import net.minecraft.client.gui.components.tabs.TabNavigationBar;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(TabNavigationBar.class)
public interface TabNavigationBarInvoker {
    @Accessor("tabManager")
    TabManager getTabManager();
}
