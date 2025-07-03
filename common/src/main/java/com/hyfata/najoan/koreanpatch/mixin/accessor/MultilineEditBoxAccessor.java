package com.hyfata.najoan.koreanpatch.mixin.accessor;

import net.minecraft.client.gui.components.MultiLineEditBox;
import net.minecraft.client.gui.components.MultilineTextField;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MultiLineEditBox.class)
public interface MultilineEditBoxAccessor {
    @Accessor
    MultilineTextField getTextField();
}
