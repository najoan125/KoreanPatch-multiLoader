package com.hyfata.najoan.koreanpatch.util.mixin.selectionmanager;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public interface ITextFieldHelperAccessor {
    int fabric_koreanchat$getCursor();
    Supplier<String> fabric_koreanchat$getStringGetter();
    Predicate<String> fabric_koreanchat$getStringFilter();
    Consumer<String> fabric_koreanchat$getStringSetter();
    String fabric_koreanchat$selectedText(String string);
    void fabric_koreanchat$runInsert(String string, String insertion);

}
