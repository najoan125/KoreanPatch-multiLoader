package com.hyfata.najoan.koreanpatch.mixin.accessor;

import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CreateWorldScreen.GameTab.class)
public interface CreateWorldScreenGameTabAccessor {
    @Accessor("nameEdit")
    EditBox getNameEdit();
}
