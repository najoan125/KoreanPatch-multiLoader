package com.hyfata.najoan.koreanpatch.handler.mixin.common;

public interface IMixinCommon {
    void modifyText(char ch);
    int getCursor();
    void writeText(String str);
}
