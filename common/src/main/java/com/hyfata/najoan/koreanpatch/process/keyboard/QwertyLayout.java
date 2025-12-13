package com.hyfata.najoan.koreanpatch.process.keyboard;

public class QwertyLayout {
    private static final QwertyLayout instance = new QwertyLayout();

    private static final String LAYOUT_STRING =
            "`1234567890-=~!@#$%^&*()_+" +   // 숫자 행 (기본 + Shift)
                    "qwertyuiop[]\\QWERTYUIOP{}|" + // 첫 번째 글자 행 (기본 + Shift)
                    "asdfghjkl;'ASDFGHJKL:\"" +     // 두 번째 글자 행 (기본 + Shift)
                    "zxcvbnm,./ZXCVBNM<>?";         // 세 번째 글자 행 (기본 + Shift)

    public static QwertyLayout getInstance() {
        return instance;
    }

    public String getLayoutString() {
        return LAYOUT_STRING;
    }
}
