package com.hyfata.najoan.koreanpatch.mixin.mods.axiom;

import com.hyfata.najoan.koreanpatch.client.KoreanPatchClient;
import com.moulberry.axiom.editor.EditorUI;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = {EditorUI.class})
public class EditorUIMixin {
    @Inject(method = "drawOverlay", at = @At("TAIL"), remap = false)
    private static void drawOverlay(CallbackInfo ci) {
        KoreanPatchClient.axiomEditorUIOpened = EditorUI.isEnabled();
    }
}
