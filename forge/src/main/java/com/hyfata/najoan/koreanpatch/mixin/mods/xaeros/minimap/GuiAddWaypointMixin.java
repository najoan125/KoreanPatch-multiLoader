package com.hyfata.najoan.koreanpatch.mixin.mods.xaeros.minimap;

import net.minecraft.client.gui.components.EditBox;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xaero.common.gui.GuiAddWaypoint;
import xaero.common.gui.WaypointEditForm;

@Mixin(GuiAddWaypoint.class)
public abstract class GuiAddWaypointMixin {
    @Shadow(remap = false)
    protected abstract WaypointEditForm getCurrent();

    @Shadow(remap = false)
    private EditBox nameTextField;

    @Shadow(remap = false)
    private EditBox initialTextField;

    @Inject(method = "renderPreDropdown", at = @At(value = "HEAD"), remap = false)
    protected void checkFields(CallbackInfo ci) {
        WaypointEditForm current = this.getCurrent();
        current.name = nameTextField.getValue();
        current.initial = this.initialTextField.getValue();
        if (current.initial.length() > 2) {
            current.initial = current.initial.substring(0, 2);
            this.initialTextField.setValue(current.initial);
        }
    }
}
