package com.hyfata.najoan.koreanpatch.mixin;

import com.hyfata.najoan.koreanpatch.mixin.accessor.OptionsAccessor;
import com.hyfata.najoan.koreanpatch.util.minecraft.KeyMappingUtil;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Options;
import org.apache.commons.lang3.ArrayUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Options.class)
public class OptionsMixin {
    @Inject(method = "load", at = @At("HEAD"))
    public void load(CallbackInfo ci) {
        OptionsAccessor accessor = (OptionsAccessor) this;
        for (KeyMapping keyMapping : KeyMappingUtil.getModdedKeyBindings()) {
            accessor.setKeyMappings(ArrayUtils.add(accessor.getKeyMappings(), keyMapping));
        }
    }
}
