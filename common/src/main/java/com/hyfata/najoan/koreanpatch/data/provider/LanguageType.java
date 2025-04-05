package com.hyfata.najoan.koreanpatch.data.provider;

import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

public enum LanguageType {
    KO("koreanpatch.langtype.korean"),
    EN("koreanpatch.langtype.english");

    final String text;

    private static boolean ime = false;

    LanguageType(String text) {
        this.text = text;
    }

    public FormattedCharSequence getTranslatedVisualOrderText() {
        return Component.translatable(text).getVisualOrderText();
    }

    public static void setIME(boolean ime) {
        LanguageType.ime = ime;
    }

    public static boolean isIME() {
        return ime;
    }
}
