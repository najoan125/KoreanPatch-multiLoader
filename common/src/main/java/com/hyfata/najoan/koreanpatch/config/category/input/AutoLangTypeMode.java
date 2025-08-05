package com.hyfata.najoan.koreanpatch.config.category.input;

import com.hyfata.najoan.koreanpatch.client.Constants;

public enum AutoLangTypeMode {
    AUTO(Constants.MOD_ID + ".config.input.mode.auto"),
    KOREAN(Constants.MOD_ID + ".config.input.mode.korean"),
    ENGLISH(Constants.MOD_ID + ".config.input.mode.english"),
    IME(Constants.MOD_ID + ".config.input.mode.ime");

    final String translatable;

    AutoLangTypeMode(String translatable) {
        this.translatable = translatable;
    }

    public String getTranslatable() {
        return translatable;
    }
}
