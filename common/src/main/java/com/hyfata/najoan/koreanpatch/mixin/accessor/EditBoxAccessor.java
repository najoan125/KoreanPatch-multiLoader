package com.hyfata.najoan.koreanpatch.mixin.accessor;

import net.minecraft.client.gui.components.EditBox;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.function.Consumer;

@Mixin(EditBox.class)
public interface EditBoxAccessor {
    @Accessor("displayPos")
    int getDisplayPos();

    @Accessor("cursorPos")
    int getCursorPos();

    @Accessor("responder")
    Consumer<String> getChangedListener();

    @Invoker("getCursorPosition")
    int getCursorPosition();

    @Invoker("moveCursorTo")
    void moveCursorTo(int var1, boolean shift);

    @Invoker("deleteChars")
    void deleteChars(int var1);

    @Invoker("getValue")
    String getValue();

    @Invoker("insertText")
    void insertText(String var1);

    @Invoker("onValueChange")
    void changed(String var1);

    @Invoker("setValue")
    void setValue(String var1);

    @Invoker("canConsumeInput")
    boolean canConsumeInput();

    @Invoker("getHighlighted")
    String getHighlighted();
}