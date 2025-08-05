package com.hyfata.najoan.koreanpatch.storage;

import com.hyfata.najoan.koreanpatch.process.LanguageType;

import java.io.Serializable;

public class InputStatus implements Serializable {
    private LanguageType languageType;
    private boolean imeFocus;

    public InputStatus(LanguageType languageType, boolean imeFocus) {
        this.languageType = languageType;
        this.imeFocus = imeFocus;
    }

    public LanguageType getLanguageType() {
        return languageType;
    }

    public void setLanguageType(LanguageType languageType) {
        this.languageType = languageType;
    }

    public boolean isImeFocus() {
        return imeFocus;
    }

    public void setImeFocus(boolean imeFocus) {
        this.imeFocus = imeFocus;
    }
}
