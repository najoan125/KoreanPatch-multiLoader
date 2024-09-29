package com.hyfata.najoan.koreanpatch.mixin.accessor;

import net.minecraft.client.gui.font.TextFieldHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

@Mixin(TextFieldHelper.class)
public interface TextFieldHelperAccessor {
    @Accessor("selectionPos")
    int getCursor();

    @Accessor("getMessageFn")
    Supplier<String> getStringGetter();

    @Accessor("stringValidator")
    Predicate<String> getStringFilter();

    @Accessor("setMessageFn")
    Consumer<String> getStringSetter();

    @Invoker("getSelected")
    String selectedText(String string);

    @Invoker("insertText")
    void runInsert(String string, String insertion);
}
