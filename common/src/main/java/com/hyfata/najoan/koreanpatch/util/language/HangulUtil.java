package com.hyfata.najoan.koreanpatch.util.language;

import com.hyfata.najoan.koreanpatch.keyboard.KeyboardLayout;
import com.hyfata.najoan.koreanpatch.keyboard.QwertyLayout;

public class HangulUtil {
    public static char getFixedHangulChar(int modifiers, char org, char hangul) {
        //Caps Lock/한글 상태면 쌍자음으로 입력되는 문제 수정
        if (HangulProcessor.isHangulCharacter(hangul)) {
            int idx = getFixedQwertyIndex(org, modifiers);
            if (idx != -1) {
                hangul = KeyboardLayout.INSTANCE.layout.toCharArray()[idx];
            }
        } //모두 소문자로 돌린 다음 shift modifier에 따라서 적절하게 처리
        return hangul;
    }
    
    public static int getFixedQwertyIndex(int chr, int modifiers) {
        boolean shift = (modifiers & 0x01) == 1;

        int codePoint = chr;

        if (codePoint >= 65 && codePoint <= 90) {
            codePoint += 32;
        }

        if (codePoint >= 97 && codePoint <= 122) {
            if (shift) {
                codePoint -= 32;
            }
        }

        return QwertyLayout.getInstance().getLayoutString().indexOf(codePoint);
    }
}
