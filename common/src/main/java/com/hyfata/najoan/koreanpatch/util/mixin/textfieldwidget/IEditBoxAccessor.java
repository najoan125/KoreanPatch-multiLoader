package com.hyfata.najoan.koreanpatch.util.mixin.textfieldwidget;

import java.util.function.Consumer;

public interface IEditBoxAccessor {
    Consumer<String> fabric_koreanchat$getChangedListener();
    int getCursorPosition();
    void moveCursorTo(int var1, boolean shift);
    void deleteChars(int var1);
    String getValue();
    void insertText(String var1);
    void fabric_koreanchat$changed(String var1);
    void setValue(String var1);
    boolean canConsumeInput();
    String getHighlighted();
}
