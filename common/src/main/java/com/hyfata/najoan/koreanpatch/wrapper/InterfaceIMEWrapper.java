package com.hyfata.najoan.koreanpatch.wrapper;

public interface InterfaceIMEWrapper {
    void modifyText(String str);
    void modifyText(char chr);
    int getCursor();
    void writeText(String str);
}
