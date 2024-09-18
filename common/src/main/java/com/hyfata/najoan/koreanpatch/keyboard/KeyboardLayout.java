package com.hyfata.najoan.koreanpatch.keyboard;

import com.google.common.base.Splitter;

import java.util.List;

public class KeyboardLayout {
    public final String layout = "`1234567890-=~!@#$%^&*()_+ㅂㅈㄷㄱㅅㅛㅕㅑㅐㅔ[]\\ㅃㅉㄸㄲㅆㅛㅕㅑㅒㅖ{}|ㅁㄴㅇㄹㅎㅗㅓㅏㅣ;'ㅁㄴㅇㄹㅎㅗㅓㅏㅣ:\"ㅋㅌㅊㅍㅠㅜㅡ,./ㅋㅌㅊㅍㅠㅜㅡ<>?";
    public final String chosung_table = "ㄱㄲㄴㄷㄸㄹㅁㅂㅃㅅㅆㅇㅈㅉㅊㅋㅌㅍㅎ";
    public final String jungsung_table = "ㅏㅐㅑㅒㅓㅔㅕㅖㅗㅘㅙㅚㅛㅜㅝㅞㅟㅠㅡㅢㅣ";
    public final String jongsung_table = "\u0000ㄱㄲㄳㄴㄵㄶㄷㄹㄺㄻㄼㄽㄾㄿㅀㅁㅂㅄㅅㅆㅇㅈㅊㅋㅌㅍㅎ";
    public final List<String> jungsung_ref_table = Splitter.on(",").splitToList(",,,,,,,,,ㅗㅏ,ㅗㅐ,ㅗㅣ,,,ㅜㅓ,ㅜㅔ,ㅜㅣ,,,ㅡㅣ,ㅣ");
    public final List<String> jongsung_ref_table = Splitter.on(",").splitToList(",,,ㄱㅅ,,ㄴㅈ,ㄴㅎ,,,ㄹㄱ,ㄹㅁ,ㄹㅂ,ㄹㅅ,ㄹㅌ,ㄹㅍ,ㄹㅎ,,,ㅂㅅ,,,,,,,,,");
    public int assemblePosition = -1;

    public static KeyboardLayout INSTANCE = new KeyboardLayout();

    public int getQwertyIndexCodePoint(char ch) {
        return QwertyLayout.getInstance().getLayoutString().indexOf(ch);
    }
}

