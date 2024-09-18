package com.hyfata.najoan.koreanpatch.util.mixin;

public interface IMixinCommon {
    void modifyText(char ch);
    int getCursor();
    void writeText(String str);
}
