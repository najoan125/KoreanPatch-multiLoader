package com.hyfata.najoan.koreanpatch.mixin.accessor;

import net.minecraft.client.gui.components.MultilineTextField;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(MultilineTextField.class)
public interface MultilineTextFieldAccessor {
    @Invoker("cursor")
    int getCursor();

    @Invoker("value")
    String getValue();

    @Invoker("insertText")
    void invokeInsertText(String text);

    @Invoker("hasSelection")
    boolean invokeHasSelection();

    @Accessor
    void setSelectCursor(int cursor);
}
