package com.hyfata.najoan.koreanpatch.mixin.accessor;

import net.minecraft.client.gui.components.EditBox;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.function.Predicate;

@Mixin(EditBox.class)
public interface EditBoxAccessor {
    @Accessor("displayPos")
    int getDisplayPos();

    @Accessor("value")
    String getValue();

    @Accessor("value")
    void overwriteValue(String val);

    @Accessor("maxLength")
    int getMaxLength();

    @Accessor("filter")
    Predicate<String> getFilter();

    @Invoker("getCursorPosition")
    int invokeGetCursorPosition();

    @Invoker("setCursorPosition")
    void invokeSetCursorPosition(int var1);

    @Invoker("setHighlightPos")
    void invokeSetHighlightPos(int var1);

    @Invoker("onValueChange")
    void invokeOnValueChange(String var1);

    @Invoker("insertText")
    void invokeInsertText(String var1);

    @Invoker("canConsumeInput")
    boolean invokeCanConsumeInput();

    @Invoker("getHighlighted")
    String invokeGetHighlighted();
}