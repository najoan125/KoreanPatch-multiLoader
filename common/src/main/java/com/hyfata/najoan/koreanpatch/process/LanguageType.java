package com.hyfata.najoan.koreanpatch.process;

import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

import java.io.Serializable;

public enum LanguageType implements Serializable {
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
