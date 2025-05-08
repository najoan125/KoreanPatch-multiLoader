package com.hyfata.najoan.koreanpatch.process.controller.mixin.common;

public interface IMixinCommon {
    void modifyText(char ch);
    int getCursor();
    void writeText(String str);
}
