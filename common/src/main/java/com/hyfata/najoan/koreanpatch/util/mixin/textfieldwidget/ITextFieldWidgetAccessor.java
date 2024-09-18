package com.hyfata.najoan.koreanpatch.util.mixin.textfieldwidget;

import java.util.function.Consumer;

public interface ITextFieldWidgetAccessor {
    Consumer<String> fabric_koreanchat$getChangedListener();
    int getCursor();
    void setCursor(int var1, boolean shift);
    void eraseCharacters(int var1);
    String getText();
    void write(String var1);
    void fabric_koreanchat$changed(String var1);
    void setText(String var1);
    boolean isActive();
    String getSelectedText();
}
