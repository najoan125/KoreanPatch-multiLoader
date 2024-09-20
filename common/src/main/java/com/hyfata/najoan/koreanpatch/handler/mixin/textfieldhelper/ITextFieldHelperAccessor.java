package com.hyfata.najoan.koreanpatch.handler.mixin.textfieldhelper;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public interface ITextFieldHelperAccessor {
    int koreanPatch$getCursor();
    Supplier<String> koreanPatch$getStringGetter();
    Predicate<String> koreanPatch$getStringFilter();
    Consumer<String> koreanPatch$getStringSetter();
    String koreanPatch$selectedText(String string);
    void koreanPatch$runInsert(String string, String insertion);

}
