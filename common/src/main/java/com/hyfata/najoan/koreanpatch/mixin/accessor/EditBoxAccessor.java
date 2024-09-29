package com.hyfata.najoan.koreanpatch.mixin.accessor;

import net.minecraft.client.gui.components.EditBox;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.function.Consumer;

@Mixin(EditBox.class)
public interface EditBoxAccessor {
    @Accessor
    int getDisplayPos();

    @Accessor
    Consumer<String> getResponder();

    @Invoker("getCursorPosition")
    int invokeGetCursorPosition();

    @Invoker("getValue")
    String invokeGetValue();

    @Invoker("moveCursorTo")
    void invokeMoveCursorTo(int pDelta, boolean pSelect);

    @Invoker("deleteChars")
    void invokeDeleteChars(int var1);

    @Invoker("insertText")
    void invokeInsertText(String var1);

    @Invoker("onValueChange")
    void invokeChanged(String var1);

    @Invoker("canConsumeInput")
    boolean invokeCanConsumeInput();

    @Invoker("getHighlighted")
    String invokeGetHighlighted();
}