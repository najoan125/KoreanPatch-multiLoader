package com.hyfata.najoan.koreanpatch.data.provider;

import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

public enum LanguageType {
    KO("koreanpatch.langtype.korean"),
    EN("koreanpatch.langtype.english");

    final String text;

    LanguageType(String text) {
        this.text = text;
    }

    public FormattedCharSequence getTranslatedVisualOrderText() {
        return Component.translatable(text).getVisualOrderText();
    }
}
