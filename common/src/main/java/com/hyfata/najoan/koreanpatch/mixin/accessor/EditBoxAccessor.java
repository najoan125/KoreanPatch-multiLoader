package com.hyfata.najoan.koreanpatch.mixin.accessor;

import net.minecraft.client.gui.components.EditBox;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(EditBox.class)
public interface EditBoxAccessor {
    @Accessor("displayPos")
    int getDisplayPos();

    @Invoker("getValue")
    String invokeGetValue();

    @Invoker("getCursorPosition")
    int invokeGetCursorPosition();

    @Invoker("setHighlightPos")
    void invokeSetHighlightPos(int var1);

    @Invoker("insertText")
    void invokeInsertText(String var1);

    @Invoker("canConsumeInput")
    boolean invokeCanConsumeInput();

    @Invoker("getHighlighted")
    String invokeGetHighlighted();
}