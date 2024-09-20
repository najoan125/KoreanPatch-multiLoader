package com.hyfata.najoan.koreanpatch.handler.mixin.editbox;

import java.util.function.Consumer;

public interface IEditBoxAccessor {
    Consumer<String> koreanPatch$getChangedListener();
    int getCursorPosition();
    void moveCursorTo(int var1, boolean shift);
    void deleteChars(int var1);
    String getValue();
    void insertText(String var1);
    void koreanPatch$changed(String var1);
    void setValue(String var1);
    boolean canConsumeInput();
    String getHighlighted();
}
