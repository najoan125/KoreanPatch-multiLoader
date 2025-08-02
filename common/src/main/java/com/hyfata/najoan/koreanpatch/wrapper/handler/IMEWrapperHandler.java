package com.hyfata.najoan.koreanpatch.wrapper.handler;

import com.hyfata.najoan.koreanpatch.process.keyboard.KeyboardLayout;
import com.hyfata.najoan.koreanpatch.wrapper.InterfaceIMEWrapper;
import com.hyfata.najoan.koreanpatch.process.HangulProcessor;
import com.hyfata.najoan.koreanpatch.util.HangulUtil;

public class IMEWrapperHandler {
    public static boolean onBackspaceKeyPressed(InterfaceIMEWrapper wrapper, int cursorPosition, String text) {
        if (cursorPosition == 0 || cursorPosition != KeyboardLayout.INSTANCE.assemblePosition) return false;

        char ch = text.toCharArray()[cursorPosition - 1];

        if (HangulProcessor.isHangulSyllables(ch)) {
            int code = ch - 0xAC00;
            int cho = code / (21 * 28);
            int jung = (code % (21 * 28)) / 28;
            int jong = (code % (21 * 28)) % 28;

            char[] ch_arr;
            if (jong != 0) {
                ch_arr = KeyboardLayout.INSTANCE.jongsung_ref_table.get(jong).toCharArray();
                if (ch_arr.length == 2) {
                    jong = KeyboardLayout.INSTANCE.jongsung_table.indexOf(ch_arr[0]);
                } else {
                    jong = 0;
                }
                char c = HangulProcessor.synthesizeHangulCharacter(cho, jung, jong);
                wrapper.modifyText(c);
            } else {
                ch_arr = KeyboardLayout.INSTANCE.jungsung_ref_table.get(jung).toCharArray();
                if (ch_arr.length == 2) {
                    jung = KeyboardLayout.INSTANCE.jungsung_table.indexOf(ch_arr[0]);
                    char c = HangulProcessor.synthesizeHangulCharacter(cho, jung, 0);
                    wrapper.modifyText(c);
                } else {
                    char c = KeyboardLayout.INSTANCE.chosung_table.charAt(cho);
                    wrapper.modifyText(c);
                }
            }
            return true;
        } else if (HangulProcessor.isHangulCharacter(ch)) {
            KeyboardLayout.INSTANCE.assemblePosition = -1;
            return false;
        }
        return false;
    }

    public static boolean onHangulCharTyped(InterfaceIMEWrapper wrapper, int keyCode, int modifiers, String text, boolean selectedTextEmpty) {
        int idx = HangulUtil.getFixedQwertyIndex(keyCode, modifiers);
        // System.out.println(String.format("idx: %d", idx));
        if (idx == -1) {
            KeyboardLayout.INSTANCE.assemblePosition = -1;
            return false;
        }

        int cursorPosition = wrapper.getCursor();

        char prev = text.toCharArray()[cursorPosition - 1];
        char curr = KeyboardLayout.INSTANCE.layout.toCharArray()[idx];

        if (cursorPosition == KeyboardLayout.INSTANCE.assemblePosition && selectedTextEmpty) {
            // 자음 + 모음
            if (HangulProcessor.isJaeum(prev) && HangulProcessor.isMoeum(curr)) {
                int cho = KeyboardLayout.INSTANCE.chosung_table.indexOf(prev);
                int jung = KeyboardLayout.INSTANCE.jungsung_table.indexOf(curr);
                char c = HangulProcessor.synthesizeHangulCharacter(cho, jung, 0);
                wrapper.modifyText(c);
                KeyboardLayout.INSTANCE.assemblePosition = wrapper.getCursor();
                return true;
            }

            if (HangulProcessor.isHangulSyllables(prev)) {
                int code = prev - 0xAC00;
                int cho = code / (21 * 28);
                int jung = (code % (21 * 28)) / 28;
                int jong = (code % (21 * 28)) % 28;

                // 중성 합성 (ㅘ, ㅙ)..
                if (jong == 0 && HangulProcessor.isJungsung(prev, curr)) {
                    jung = HangulProcessor.getJungsung(prev, curr);
                    char c = HangulProcessor.synthesizeHangulCharacter(cho, jung, 0);
                    wrapper.modifyText(c);
                    KeyboardLayout.INSTANCE.assemblePosition = wrapper.getCursor();
                    return true;
                }

                // 종성 추가
                if (jong == 0 && HangulProcessor.isJongsung(curr)) {
                    char c = HangulProcessor.synthesizeHangulCharacter(cho, jung, HangulProcessor.getJongsung(curr));
                    wrapper.modifyText(c);
                    KeyboardLayout.INSTANCE.assemblePosition = wrapper.getCursor();
                    return true;
                }

                // 종성 받침 추가
                if (jong != 0 && HangulProcessor.isJongsung(prev, curr)) {
                    jong = HangulProcessor.getJongsung(prev, curr);
                    char c = HangulProcessor.synthesizeHangulCharacter(cho, jung, jong);
                    wrapper.modifyText(c);
                    KeyboardLayout.INSTANCE.assemblePosition = wrapper.getCursor();
                    return true;
                }

                // 종성에서 받침 하나 빼고 글자 만들기
                if (jong != 0 && HangulProcessor.isJungsung(curr)) {
                    char[] tbl = KeyboardLayout.INSTANCE.jongsung_ref_table.get(jong).toCharArray();
                    int newCho;
                    if (tbl.length == 2) {
                        newCho = KeyboardLayout.INSTANCE.chosung_table.indexOf(tbl[1]);
                        jong = KeyboardLayout.INSTANCE.jongsung_table.indexOf(tbl[0]);
                    } else {
                        newCho = KeyboardLayout.INSTANCE.chosung_table.indexOf(KeyboardLayout.INSTANCE.jongsung_table.charAt(jong));
                        jong = 0;
                    }

                    char c = HangulProcessor.synthesizeHangulCharacter(cho, jung, jong);
                    wrapper.modifyText(c);

                    cho = newCho;
                    jung = KeyboardLayout.INSTANCE.jungsung_table.indexOf(curr);
                    code = HangulProcessor.synthesizeHangulCharacter(cho, jung, 0);
                    wrapper.writeText(String.valueOf(Character.toChars(code)));
                    KeyboardLayout.INSTANCE.assemblePosition = wrapper.getCursor();
                    return true;
                }
            }
        }

        wrapper.writeText(String.valueOf(curr));
        KeyboardLayout.INSTANCE.assemblePosition = wrapper.getCursor();
        return true;
    }
}
