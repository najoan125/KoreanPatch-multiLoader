package com.hyfata.najoan.koreanpatch.data;

import com.hyfata.najoan.koreanpatch.data.provider.LanguageType;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

public class LangTypeManager {
    private static LangTypeManager instance;

    public static LangTypeManager getInstance() {
        if (instance == null) {
            instance = new LangTypeManager();
        }
        return instance;
    }

    private final Minecraft client = Minecraft.getInstance();
    private LanguageType currentType = LanguageType.EN;
    private final Component IME_TEXT = Component.literal("IME");
    private boolean ime = false;

    public void setCurrentType(LanguageType type) {
        currentType = type;
    }

    public LanguageType getCurrentType() {
        return currentType;
    }

    public boolean isKorean() {
        return currentType == LanguageType.KO && !isIme();
    }

    public void toggleCurrentType() {
        currentType = currentType == LanguageType.KO ? LanguageType.EN : LanguageType.KO;
    }

    public FormattedCharSequence getCurrentText() {
        if (isIme()) {
            return IME_TEXT.getVisualOrderText();
        }
        return currentType.getTranslatedVisualOrderText();
    }

    public int getCurrentTextWidth() {
        return client.font.width(getCurrentText());
    }

    public boolean isIme() {
        return ime;
    }

    public void setIme(boolean ime) {
        this.ime = ime;
    }
}