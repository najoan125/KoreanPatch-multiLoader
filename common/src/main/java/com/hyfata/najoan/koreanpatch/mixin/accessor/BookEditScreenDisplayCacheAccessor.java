package com.hyfata.najoan.koreanpatch.mixin.accessor;

import net.minecraft.client.gui.screens.inventory.BookEditScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BookEditScreen.DisplayCache.class)
public interface BookEditScreenDisplayCacheAccessor {
    @Accessor("cursor")
    BookEditScreen.Pos2i getCursor();
}
