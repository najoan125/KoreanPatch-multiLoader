package com.hyfata.najoan.koreanpatch.util.language;

import com.hyfata.najoan.koreanpatch.data.provider.LanguageType;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

public class LanguageUtil {
    private static final Minecraft client = Minecraft.getInstance();
    private static LanguageType currentType = LanguageType.EN;
    private static final Component IME_TEXT = Component.literal("IME");

    public static boolean isKorean() {
        return currentType == LanguageType.KO && !LanguageType.isIME();
    }

    public static void toggleCurrentType() {
        currentType = currentType == LanguageType.KO ? LanguageType.EN : LanguageType.KO;
    }

    public static FormattedCharSequence getCurrentText() {
        if (LanguageType.isIME()) {
            return IME_TEXT.getVisualOrderText();
        }
        return currentType.getTranslatedVisualOrderText();
    }

    public static int getCurrentTextWidth() {
        return client.font.width(getCurrentText());
    }
}