package com.hyfata.najoan.koreanpatch.data.provider.keyboard;

public class QwertyLayout {
    private static final QwertyLayout instance = new QwertyLayout();
    public static QwertyLayout getInstance() {
        return instance;
    }

    public String getLayoutString() {
        return "`1234567890-=~!@#$%^&*()_+qwertyuiop[]\\QWERTYUIOP{}|asdfghjkl;'ASDFGHJKL:\"zxcvbnm,./ZXCVBNM<>?";
    }
}
